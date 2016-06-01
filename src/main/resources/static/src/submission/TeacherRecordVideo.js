/**
 * Created by c13lbm on 4/27/16.
 */

var TeacherRecordVideo = React.createClass({
    formDataBuilder: function (blob, fileName) {
            var fd = new FormData();
            var id_array = this.props.idArray;
            var student = this.props.studentArray;

            fd.append("feedbackVideo", blob);
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
                <div className="row" id="">
                    <div id="videocontainer" className="columns">
                        <Recorder playCallback={this.playVideo}
                                  postURL={submissionURL}
                                  formDataBuilder={this.formDataBuilder}
                                  recButtonID="teacherRecordButton"
                                  stopButtonID="teacherStopButton"
                                  fileName="feedback.webm" replay="true"
                                  siteView="feedback" camOnLoad="true"
                                  contID="teacherRecorder"
                        />
                    </div>
                </div>
                <div className="row">
                    <div className="teacherButtonContainer">
                        <button id="teacherRecordButton" className="recControls">Record</button>
                        <button id="teacherStopButton" className="recControls" >Stop</button>
                    </div>
                </div>
            </div>
        );
    }
});

window.TeacherRecordVideo = TeacherRecordVideo;
