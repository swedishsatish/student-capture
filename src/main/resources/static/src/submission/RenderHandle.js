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


                <div id="allVideos">

                   <div id="studentVid">
                        <h1 id ="studentTitle">Student Video</h1>
                       <div className="feedbackvideofiller"> </div>
                            <video id="video-container" style={{"width":"80%"}} src={studentSubmission} preload="auto" controls/>
                    </div>

                   <div id="teacherFeedback">
                   <h1 id="studentTitle">My Feedback Video</h1>
                    <div className="feedbackvideofiller"> </div>
                        <video id="video-container" style={{"width":"80%"}} src={teacherFeedbackURL} preload="auto" controls />
                    </div>
                    <div id="newFeedback">
                        <h1 id="studentTitle">New Feedback Video</h1>
                   <TeacherRecordVideo studentArray={this.props.studentArray} idArray={this.props.idArray}/>
                   </div>

                </div>

                    <div id="submissioncontainer">
                        <div id="commentbox">
                            <CommentBox />
                        </div>
                        <div id="gradeAndPassContainer">

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
