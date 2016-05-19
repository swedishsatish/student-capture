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
 * camOnLoad:       camOnLoad="true" If the camera is to start before recording. User will have to click
 *                  stopbutton to be able to reuse camera.
 * autoRecord:      autoRecord="true" if the camera should start recording when class is instantiated.
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

        var cameraStartOnLoad = (typeof props.camOnLoad === "undefined") ?
                        false : props.camOnLoad == "true";
        var cameraStarted = false;
        var startRecordButtonExists = (typeof props.recButtonID !== "undefined");
        var shouldAutoRecord = (typeof props.autoRecord === "undefined") ?
                        !startRecordButtonExists : props.autoRecord == "true";

        var replay = props.replay == "true";
        var stopButton = document.getElementById(props.stopButtonID);
        var previewElement;
        var recordAudio, recordVideo;
        var mediaStream;


        if(startRecordButtonExists) {
            var recordButton = document.getElementById(props.recButtonID);
            if(cameraStarted) {
                recordButton.disabled = true;
            }
        }

        if(typeof props.calc !== "undefined")
            previewElement = document.getElementById('prev-test');
        else
            previewElement = document.getElementById('preview');

        navigator.getUserMedia = ( navigator.getUserMedia ||
                                  navigator.webkitGetUserMedia ||
        navigator.mediaDevices.getUserMedia ||
                                  navigator.msGetUserMedia);

        if(cameraStartOnLoad) {
            startCamera();
        }

        /* The onclick function for the start record button if it exists. */
        if(startRecordButtonExists) {
            recordButton.onclick = startRecord;
        }

        /* The onclick function for the stop record button. */
        stopButton.onclick = stopRecording;

        /* Will start recording and start the webcam if not enabled. */
        function startRecord() {
            if(!cameraStarted) {
                startCamera();
                window.setTimeout(function() {
                    recordAudio.startRecording();
                    recordingStarted();
                }, 1000);
            } else {
                recordAudio.startRecording();
                recordingStarted();
            }
        }

        function recordingStarted() {
            if(startRecordButtonExists) {
                recordButton.disabled = true;
            }
            stopButton.disabled = false;

            if(typeof props.calc !== "undefined") {
                document.getElementById("test-rec-text").innerHTML = "&#x1f534;";
            }
            else {
                document.getElementById("rec-text").innerHTML = "&#x1f534;";
            }
        }

        /* Start stream from webcam and start recording if autoRecording is true,
         * will record with forced low settings for smaller files. */
        function startCamera () {
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
                mediaStream = stream;
                previewElement.src = window.URL.createObjectURL(mediaStream);
                previewElement.removeAttribute("controls");
                previewElement.setAttribute("muted", "muted");
                window.setTimeout(function() { previewElement.play(); }, 200);

                recordAudio = RecordRTC(mediaStream, {
                    onAudioProcessStarted: function () {
                        recordVideo.startRecording();
                    }
                });

                recordVideo = RecordRTC(mediaStream, {
                    type: 'video'
                });

                if(startRecordButtonExists && cameraStartOnLoad) {
                    recordButton.disabled = false;
                }

                if(shouldAutoRecord) {
                    startRecord();
                }
                cameraStarted = true;
            }, function (error) {
                alert("Problem occured, make sure camera is not\nused elsewhere and that you are connected\nby https.");
            });
        }

        /* Closes the webcam stream and post to server if auto recording is on. */
        function stopRecording() {
            if(!shouldAutoRecord){
                recordButton.disabled = false;
            }

            stopButton.disabled = true;

            previewElement.src = '';

            var postbutton = null;
            if (typeof props.postButtonID !== "undefined") {
                postbutton = document.getElementById(props.postButtonID);
            }

            recordVideo.stopRecording(function (url) {
                if(replay) {
                    previewElement.src = url;
                    previewElement.setAttribute("controls","controls");
                    previewElement.removeAttribute("muted");
                }

                if(postbutton == null) {
                    if(props.siteView !== null) {
                        PostBlob(recordVideo.getBlob(), props.siteView);
                    } else {
                        PostBlob(recordVideo.getBlob());
                    }
                }
                else {
                    previewElement.src = url;
                    previewElement.setAttribute("controls","controls");
                    previewElement.removeAttribute("muted");
                    postbutton.disabled = false;
                    postbutton.onclick = function () {
                        if(props.siteView !== null) {
                            PostBlob(recordVideo.getBlob(), props.siteView);
                        } else {
                            PostBlob(recordVideo.getBlob());
                        }
                    }
                }
                mediaStream.stop();
                mediaStream = null;
                if(typeof props.calc !== "undefined") {
                    document.getElementById("test-rec-text").innerHTML = "&#11093;";
                }
                else {
                    document.getElementById("rec-text").innerHTML = "&#11093;";
                }
                cameraStarted = false;
            });
            var hasModal = document.getElementById("assignment-modal");
            if (hasModal !== null) {
                hasModal.style.display = 'none';
            }
        }

        /* Post to the server */
        function PostBlob(blob, siteView) {
            // FormData - data to be sent to the server
            var formData = props.formDataBuilder(blob,props.fileName);

            //used for hw testing
            if(typeof props.calc !== "undefined") {
              blobsize = blob.size / 1048576;
              sendTime = Date.now();
            }

            //call xhr with full url, data and callback function
            xhr(props.postURL, formData, props.playCallback);
        }

        /* Function for sending XMLHttpRequests. */
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

        /* Showing recording-light on load (not recording) */

        if(typeof props.calc !== "undefined") {
            document.getElementById("test-rec-text").innerHTML = "&#11093;";
        }
        else {
            document.getElementById("rec-text").innerHTML = "&#11093;";
        }
    },
    render: function() {
        var id;
        var pId;
        var contId;
        if(typeof this.props.calc !== "undefined") {
            id="prev-test";
            pId="test-rec-text";
            contId = "test-prev-container";
        }
        else {
            id="preview";
            pId="rec-text";
            contId = "prev-container";
        }

        return (
            <div>
                <p id={pId}></p>
                <div id={contId}>
                    <video id={id} muted ></video>
                </div>
            </div>
        );
    }
});

window.Recorder = Recorder;