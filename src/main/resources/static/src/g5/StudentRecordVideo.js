/**
 * Created by c13lbm on 4/27/16.
 */

var StudentRecordVideo = React.createClass({
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();

        fd.append("studentVideo", blob);

        fd.append('submission', new Blob([JSON.stringify({
            // Submission data
            studentID: this.props.studentID,
            assignmentID: this.props.assignmentID
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
                <div id="recorder-div">
                    <Recorder playCallback={this.playVideo}
                              postURL={"assignments/"+this.props.assignmentID+"/submissions/"+this.props.studentID}
                              formDataBuilder={this.formDataBuilder}
                              stopButtonID="studentSubmit" autoRecord={autoRecord}
                              siteView="submission" fileName="submission.webm"
                              camOnLoad="true"
                    />
                </div>
                <button id="studentSubmit" className="recControls" disabled>Submit answer</button>
                <BlankBox/>
            </div>
        );
    },
    componentDidMount: function() {
        var vid = document.getElementById("recorder-div");
        vid.oncontextmenu = function (e) {e.preventDefault();};
    }
});

window.StudentRecordVideo = StudentRecordVideo;

