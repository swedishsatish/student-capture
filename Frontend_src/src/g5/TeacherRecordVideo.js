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

/*
        var container = document.getElementById("videocontainer");
        if (container.childNodes.length > 1) {
            container.removeChild(container.childNodes.item(0));
        }

        var mediaElement = document.createElement("video");

        var source = document.createElement('source');
        source.src = "data:video/webm;base64," + fName;

        source.type = 'video/webm; codecs="vp8, vorbis"';

        mediaElement.setAttribute("width", "100%");
        mediaElement.setAttribute("height", "100%");
        mediaElement.appendChild(source);
        mediaElement.controls = true;
        container.appendChild(mediaElement)
        mediaElement.play();
*/
    },
//    calcSpeed: function (blobsize, sendTime) {

//        $("#internet-speed").text(function () {
//            var now = Date.now();
//            var mbsec = (blobsize / ((now - sendTime) / 1000));
//            //console.log("mbsec " + mbsec + "now = " + now/1000 + "stime" + sendTime/1000);
//            return "Upload speed = " + mbsec.toFixed(2) + "MB/s"
//        });
//    },
    render: function () {

        return (
            <div>
                <h3>Teacher Recording Video</h3>
                <div className="row" id="">
                    <div id="videocontainer" className="six columns">
                        <Recorder playCallback={this.playVideo}
                                  postURL="/uploadVideo/" formDataBuilder={this.formDataBuilder}
                                  recButtonID="record-test" stopButtonID="stop-test" fileName="assignment.webm"
                                  replay="true" postButtonID="postVideo" siteView="createAssignment"
                        />


                    </div>

                </div>
                <div className="row">
                    <div className="four columns u-pull-left">
                        <button id="record-test" className="recControls">Record</button>

                        <button id="stop-test" className="recControls" disabled>Stop</button>
                        <button id="postVideo" className="recControls" disabled>POST</button>

                    </div>

                </div>
            </div>
        );
    }
});

window.TeacherRecordVideo = TeacherRecordVideo;
