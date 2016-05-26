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
        console.log(this.props.studentArray);
        console.log("hejhej" + this.props.toString());

        var id_array = this.props.idArray;
        var student = this.props.studentArray;
        var filenm = '/submission.webm';
        var videoSource = "assignments/" + id_array[0].assignmentID + "/submissions/" + student[0].studentID + "/videos" + filenm;

        return(
            <div class="row">
                <div id="blanket" style={{"display":"none"}}></div>
                <div id="popUpDiv" style={{"display":"none"}}></div>

                <video width="720" height="460" src={videoSource} preload="auto" controls id="teststudentvideo"/>
                <TeacherRecordVideo setVideo={this.setVideo}/>

                <div id="feedBackContainer">

                    <div id="submissioncontainer">
                        <div id="commentbox">
                            <CommentBox />
                        </div>
                        <div id="gradeAndPassContainer">
                            <div id="passbox">
                                <PermissionCheckBox/>
                                <PassCheckBox />
                            </div>
                            <div id="dropDown">
                                <DropDown scale={this.props.scale}/>
                            </div>
                        </div>
                    </div>
                    <div id="buttonContainer">
                        <div id="backButton">
                            <BackButton idArray={this.props.idArray} />
                        </div>
                        <div id="submitButton">
                            <SubmitButton getVideo={this.getVideo} studentArray={this.props.studentArray} idArray={this.props.idArray}/>
                        </div>

                    </div>
                </div>
            </div>
        )
    },

    setVideo:function(blob) {
        video = blob;
        console.log("VIDEO WAS SET");

    },

    getVideo:function() {

       return video;

    }


});

window.RenderHandle = RenderHandle;
