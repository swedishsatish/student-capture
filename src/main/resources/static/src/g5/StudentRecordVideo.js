/**
 * Created by c13lbm on 4/27/16.
 */

var StudentRecordVideo = React.createClass({
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();

        fd.append("studentVideo", blob);

        fd.append('submission', new Blob([JSON.stringify({
            // Submission data
            studentID: 12, // TODO change to this.props.studentID
            assignmentID: 50 // TODO change to this.props.assignementID
        })], {
            type: "application/json"
        }));

        return fd;
    },
    playVideo: function (fName) {
    },
    render: function () {
        var autoRecord = this.props.autoRecord;
        if(typeof autoRecord === "undefined")
            autoRecord = "true";
        return (
            <div>
                <h3>Student Recording Video</h3>

                    <h5 id="isRecording"></h5>
                        <Recorder playCallback={this.playVideo}
                                  postURL="/assignments/"+this.props.assignmentID+"/submissions/"+this.props.studentID
                                  formDataBuilder={this.formDataBuilder}
                                  stopButtonID="studentSubmit" autoRecord={autoRecord}
                                  siteView="submission" fileName="submission.webm"
                                  camOnLoad="true"
                        />


                     <button id="studentSubmit" className="recControls" disabled>Submit answer</button>
                     <BlankBox/>
            </div>

        );
    }
});

window.StudentRecordVideo = StudentRecordVideo;
