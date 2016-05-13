/**
 * Created by c13lbm on 4/27/16.
 */

var StudentRecordVideo = React.createClass({
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();
        fd.append("videoName", fileName);
        fd.append("video", blob);

        fd.append("videoType", "submission"); // submission/answer/feedback
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
            <div>
                <h3>Student Recording Video</h3>
                <div className="row" id="">
                    <div className="six columns"><h5 id="isRecording"></h5>
                        <Recorder playCallback={this.playVideo}
                                  postURL="/uploadVideo/" formDataBuilder={this.formDataBuilder}
                                  stopButtonID="studentSubmit" siteView="submission" fileName="submission.webm"
                        />
                    </div>
                </div>
                <div className="row">
                    <div className="four columns u-pull-left">
                        <button id="studentSubmit" className="recControls" disabled>Submit answer</button>
                    </div>
                </div>
            </div>
        );
    }
});

window.StudentRecordVideo = StudentRecordVideo;
