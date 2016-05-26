/**
 * Created by c13lbm on 4/27/16.
 */

var StudentRecordVideo = React.createClass({
    submitbutton: <div id="studentSubmit" className="button primary-button SCButtonDisabled" >Submit answer</div>,
    componentDidMount: function() {
        this.setEnabled(false);
    },
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();

        fd.append("studentVideo", blob);

        fd.append('submission', new Blob([JSON.stringify({
            // Submission data
            studentID: this.props.studentID,
            assignmentID: this.props.assignmentID,
            courseID: this.props.courseID,
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

        var submissionURL = "assignments/"+this.props.assignmentID+"/submissions/"+this.props.studentID;
        return (
            <div>
                <div id="recorder-div">
                    <Recorder id="recorder" contID="studentPreview"
                              playCallback={this.playVideo}
                              postURL={submissionURL}
                              formDataBuilder={this.formDataBuilder}
                              stopButtonID="studentSubmit" autoRecord={autoRecord}
                              siteView="submission" fileName="submission.webm"
                              camOnLoad="true" maxRecordTime={this.props.maxRecordTime}
                              minRecordTime={this.props.minRecordTime}
                              endFunc={this.props.endFunc}
                              setSubmitEnabled={this.setEnabled}
                    />
                </div>
                {this.submitbutton}
                <BlankBox postURL={submissionURL}
                            assignmentID={this.props.assignmentID}
                            studentID={this.props.studentID}
                            courseID={this.props.courseID}
                            endFunc={this.props.endFunc} />
            </div>
        );
    },
    setEnabled: function(enabled) {
        if(enabled) {
            this.submitbutton = <div id="studentSubmit" className="button primary-button SCButton" >Submit answer</div>
        } else {
            this.submitbutton = <div id="studentSubmit" className="button primary-button SCButtonDisabled" >Submit answer</div>
        }
    },
    componentDidMount: function() {
        var vid = document.getElementById("recorder-div");
        vid.oncontextmenu = function (e) {e.preventDefault();};
    }
});

window.StudentRecordVideo = StudentRecordVideo;

