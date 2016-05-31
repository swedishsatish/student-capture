/**
 * Handles rendering for teacher feedback components.
 * @author: dv13trm, c14gls, group 6
 */
var video;

/**
 * Renders all the different built components for the GUI in prioritizing order.
 * For example: First it renders Recorder, then CommentBox and so on.
*/
 var RenderHandle = React.createClass({
    /**
     * Rendering all components for teacher feedback.
     * @returns {XML} components.
     */

    render: function () {
        var id_array = this.props.idArray;
        var student = this.props.studentArray;
        var path = "assignments/" + id_array[0].assignmentID + "/submissions/" + student[0].studentID + "/videos/";
        var studentSubmission = path + 'submission.webm';
        var teacherFeedbackURL = path + 'feedback.webm';


        return(
            <div class="row">
                <div id="blanket" style={{"display":"none"}}></div>
                <div id="popUpDiv" style={{"display":"none"}}></div>



                <div id="feedBackContainer">

                <table style={{"Border":"1px solid black", "width":"100%"}}>
                    <tr><td>
                        Student
                    </td><td>
                        Feedback
                    </td><td>
                        New feedback
                    </td></tr>
                    <tr><td>
                        <video id="studentVideo" style={{"width":"80%"}} src={studentSubmission} preload="auto" controls/>
                    </td><td>
                        <video id="teacherRecordFeedback" style={{"width":"80%"}} src={teacherFeedbackURL} preload="auto" controls/>
                    </td><td>
                        <TeacherRecordVideo studentArray={this.props.studentArray} idArray={this.props.idArray}/>
                    </td></tr>
                </table>



                    <div id="submissioncontainer">
                        <div id="commentbox">
                            <CommentBox />
                        </div>
                        <div id="gradeAndPassContainer">
                            <div id="passbox">
                                <PermissionCheckBox/>
                            </div>
                            <div id="dropDown">
                                <DropDown scale={this.props.scale}/>
                            </div>
                        </div>
                    </div>
                    <div id="buttonContainer">
                        <div id="backButton">
                            <BackButton idArray={this.props.idArray} scale={this.props.scale} />
                        </div>
                        <div id="submitButton">
                            <SubmitButton scale={this.props.scale} studentArray={this.props.studentArray} idArray={this.props.idArray}/>
                        </div>

                    </div>
                </div>
            </div>
        )
    },


});

window.RenderHandle = RenderHandle;
