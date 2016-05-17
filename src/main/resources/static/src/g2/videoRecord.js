/**
 * Props params:
 *
 * playCallback:    Function to be called with response from server. should take one parameter.
 * calc:            Only used when testing hardware.
 * postURL:         The url after the globally set url.
 * formDataBuilder  Function to create formdata containing information to send. Function takes:.
 *                                                                                     video(blob) as first param
 *                                                                                     filename as second param.
 * recButtonID:     [Optional] Button to start recording video. If left out it autorecords on render.
 * stopButtonID:    Id of the button to stop recording video. Might post video as well(read about postButtonID).
 * postButtonID:    [Optional] Id of the post video button. If left out the stop button posts the video to server.
 * replay:          replay="true" if you want the recording to be playable in the same video tag.
 * fileName:        The name the video recording should have.
 * camOn:           camOn="true" If the camera is to start before recording. User will have to click stopbutton to be able
 *                  to reuse camera.
 *
 *
 * watch HardwareTest.js for example of use.
 */



var Recorder = React.createClass({
    componentDidMount: function() {
        var props = this.props;

        //used for hw testing
        var blobsize;
        var sendTime;

        var camOn = props.camOn=="true";

        var autoRec = (typeof props.recButtonID === "undefined");

        var replay = props.replay == "true";

        function PostBlob(blob, siteView) {
            // FormData - data to be sent to the server
            var formData = props.formDataBuilder(blob,props.fileName);

            //used for hw testing
            if(typeof props.calc !== "undefined") {
                blobsize = blob.size / 1048576;
                sendTime = Date.now();
            }

            //call xhr with full url, data and callback function
            if(siteView == "createAssignment" || siteView == "submission") {
                var xhReq = new XMLHttpRequest();

                xhReq.onreadystatechange = function () {
                    if (xhReq.readyState === 4 && xhReq.status == 200) {
                        console.log("id: " + xhReq.responseText);

                        xhr(window.globalURL + props.postURL + xhReq.responseText, formData, props.playCallback);
                    } else if(xhReq.readyState === 4 && xhReq.status !== 200) {
                        if(xhReq.responseText == "")
                            alert("Upload failed, no server connection.");
                        else
                            alert(xhReq.responseText);
                    }
                };
                xhReq.onload = function() {
                    if(xhReq.status == 404) {
                        alert("Upload failed, no server connection.");
                    }
                    else if(xhReq.status == 408) {
                        alert("Connection timed out.");
                    }
                    else if(xhReq.failed) {
                        alert("Upload failed, no server connection.");
                    }
                }

                var userID = "26";
                var courseID = "60";
                var assignmentID = "1000";

                var url = window.globalURL+"/video/inrequest?userID=" + userID + "&courseID=" + courseID +
                    "&assignmentID=" + assignmentID;
                var method = "GET";

                xhReq.open(method, url, true);
                xhReq.send();

            } else {
                xhr(window.globalURL + props.postURL, formData, props.playCallback);
            }
        }

        if(!autoRec) {
            var record = document.getElementById(props.recButtonID);
            if(camOn)
                record.disabled = true;
        }

        var stop = document.getElementById(props.stopButtonID);

        var preview;
        if(typeof props.calc !== "undefined")
            preview = document.getElementById('prev-test');
        else
            preview = document.getElementById('preview');

        navigator.getUserMedia = ( navigator.getUserMedia ||
        navigator.webkitGetUserMedia ||
        navigator.mediaDevices.getUserMedia ||
        navigator.msGetUserMedia);


        var recordAudio, recordVideo;
        var localStream;
        var startRecord = function () {
            if(!autoRec) {
                record.disabled = true;
            }
            stop.disabled = false;
            //startCam();

            recordAudio.startRecording();
            if(typeof props.calc !== "undefined") {
                document.getElementById("test-rec-text").innerHTML = "Recording..";
            }
            else {
                document.getElementById("rec-text").innerHTML = "Recording..";
            }
        };

        // Start stream from webcam and start record if autoRecording is true,
        // will record with forced low settings for smaller files.
        var startCam = function () {
            navigator.getUserMedia({
                audio: true,
                video: {
                    mandatory: {
                        minWidth: 160,
                        maxWidth: 320,
                        minHeight: 120,
                        maxHeight: 240,
                        minFrameRate: 5,
                        maxFrameRate: 10
                    }
                }
            }, function (stream) {
                preview.src = window.URL.createObjectURL(stream);
                preview.play();
                localStream = stream;

                recordAudio = RecordRTC(stream, {
                    onAudioProcessStarted: function () {
                        recordVideo.startRecording();
                    }
                });

                recordVideo = RecordRTC(stream, {
                    type: 'video'
                });

                recordAudio = RecordRTC(localStream, {
                    onAudioProcessStarted: function () {
                        recordVideo.startRecording();
                    }
                });

                recordVideo = RecordRTC(localStream, {
                    type: 'video'
                });

                if(!autoRec && camOn) {

                    record.disabled = false;


                }else {
                    startRecord();
                }

            }, function (error) {
                alert("Problem occured, make sure camera is not\nused elsewhere and that you are connected\nby https.");
            });

        }
        console.log(camOn)
        if(!autoRec) {
            if(camOn)
                record.onclick = startRecord;
            else
                record.onclick = startCam;
        }

        stop.onclick = function () {
            if(!autoRec){
                record.disabled = false;
            }

            stop.disabled = true;

            preview.src = '';

            var postbutton = null;
            if (typeof props.postButtonID !== "undefined") {
                postbutton = document.getElementById(props.postButtonID);
            }

            recordVideo.stopRecording(function (url) {
                if(replay){
                    preview.src = url;
                    preview.setAttribute("controls","controls");
                    preview.removeAttribute("muted");
                }

                if(postbutton == null) {
                    if(props.siteView !== null) {
                        PostBlob(recordVideo.getBlob(), props.siteView);
                    } else {
                        PostBlob(recordVideo.getBlob());
                    }
                }
                else {
                    preview.src = url;
                    preview.setAttribute("controls","controls");
                    preview.removeAttribute("muted");
                    postbutton.disabled = false;
                    postbutton.onclick = function () {
                        if(props.siteView !== null) {
                            PostBlob(recordVideo.getBlob(), props.siteView);
                        } else {
                            PostBlob(recordVideo.getBlob());
                        }
                    }
                }
                localStream.stop();
                localStream = null;
                if(typeof props.calc !== "undefined") {
                    document.getElementById("test-rec-text").innerHTML = "";
                }
                else {
                    document.getElementById("rec-text").innerHTML = "";
                }


            });
            var hasModal = document.getElementById("assignment-modal");
            if (hasModal !== null) {
                hasModal.style.display = 'none';
            }
        };

        function xhr(url, data, callback) {
            var request = new XMLHttpRequest();
            request.onreadystatechange = function () {
                if (request.readyState == 4 && request.status == 200) {
                    callback(request.responseText);
                } else if(request.readyState == 4 && request.status !== 200) {
                    alert(request.responseText);
                }
            };

            if(typeof props.calc !== "undefined") {
                request.upload.onloadstart = function () {
                    $("#internet-speed").text("Uploading...");
                }
                request.onloadstart = function () {
                    $("#internet-speed").text("Uploading...");
                }
            }
            request.onload = function(){
                if(typeof props.calc !== "undefined") {
                    if(request.status == 404)
                        $("#internet-speed").text("Upload failed, no server connection.");
                    else if(request.status == 408)
                        $("#internet-speed").text("Connection timed out.");
                    else
                        props.calc(blobsize,sendTime);
                }
                else if(request.status == 404) {
                    alert("Upload failed, no server connection.");
                }
                else if(request.status == 408) {
                    alert("Connection timed out.");
                }
            }

            request.open('POST', url,true);
            request.send(data);
        }


        if(camOn){

            startCam();
        }

    },
    render: function() {
        var id;
        var pId;
        if(typeof this.props.calc !== "undefined") {
            id="prev-test";
            pId="test-rec-text"
        }
        else {
            id="preview";
            pId="rec-text"
        }

        return (
            <div>
                <div id="prev-container">
                    <video id={id} muted ></video>
                </div>
                <p id={pId}></p>
            </div>
        );
    }
});

window.Recorder = Recorder;