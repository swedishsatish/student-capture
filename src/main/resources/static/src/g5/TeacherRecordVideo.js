/**
 * Created by c13lbm on 4/27/16.
 */

var TeacherRecordVideo = React.createClass({
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();
        fd.append("videoName", fileName);
        fd.append("video", blob);

        fd.append("videoType", "assignment"); // submission/question/feedback
        fd.append("userID", "26");
        fd.append("assignmentID", "60");
        fd.append("courseID", "1000");
        fd.append("courseCode", "1000");

        return fd;
    },
    playVideo: function (fName) {
    },
    render: function () {
        return (
            <div id="teacherRecord">
                <h3>Teacher Recording Video</h3>
                <div className="row" id="">
                    <div id="videocontainer" className="six columns">
                        <Recorder playCallback={this.playVideo}
                                  postURL="uploadVideo/" formDataBuilder={this.formDataBuilder} setVideo={this.props.setVideo}
                                  recButtonID="teacherRecordButton" stopButtonID="teacherStopButton"
                                  fileName="assignment.webm" replay="true" postButtonID="postTeacherVideo"
                                  siteView="feedback" camOnLoad="true"
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
