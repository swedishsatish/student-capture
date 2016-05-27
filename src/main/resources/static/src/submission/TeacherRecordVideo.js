/**
 * Created by c13lbm on 4/27/16.
 */

var TeacherRecordVideo = React.createClass({
    formDataBuilder: function (blob, fileName) {
            var fd = new FormData();

            var id_array = this.props.idArray;
            var student = this.props.studentArray;

            fd.append("feedbackVideo", blob);
            fd.append('submission', new Blob([JSON.stringify({
                        // Submission data
                        studentID: student[0].studentID,
                        assignmentID: id_array[0].assignmentID,
                        courseID: id_array[0].courseID,


            })], {
                type: "application/json"
            }));

            return fd;
       },
    playVideo: function (fName) {
    },
    render: function () {
        var id_array = this.props.idArray;
        var student = this.props.studentArray;
        var submissionURL = "assignments/" + id_array[0].assignmentID + "/submissions/" + student[0].studentID + "/feedbackvideo/";
        return (
            <div id="teacherRecord">
                <h3>Teacher Recording Video</h3>
                <div className="row" id="">
                    <div id="videocontainer" className="six columns">
                        <Recorder playCallback={this.playVideo}
                                  postURL={submissionURL}
                                  formDataBuilder={this.formDataBuilder}
                                  recButtonID="teacherRecordButton"
                                  stopButtonID="teacherStopButton"
                                  fileName="feedback.webm" replay="true"
                                  postButtonID="postTeacherVideo"
                                  siteView="feedback" camOnLoad="true"
                                  contID="teacherRecorder"
                        />
                    </div>
                </div>
                <div className="row">
                    <div className="four columns u-pull-left">
                        <button id="teacherRecordButton" className="recControls">Record</button>
                        <button id="teacherStopButton" className="recControls" disabled>Stop</button>
                        <button id="postTeacherVideo" className="recControls" disabled>POST</button>
                    </div>
                </div>
            </div>
        );
    }
});

window.TeacherRecordVideo = TeacherRecordVideo;
