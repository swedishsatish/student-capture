/**
 *@author Ludvig Boström <c13lbm>
 *@revision: Fredrik Mörtberg <c13fmg>
 */

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

var mediaStream = null;
var minrecordTimer;
var maxrecordTimer;

function closeStream() {
    if(mediaStream != null && typeof mediaStream !== "undefined"){
        mediaStream.stop();
        mediaStream = null;
    }
}

var Recorder = React.createClass({
    defaultMinTime: 10,
    defaultMaxTime: 120,

    componentDidMount: function() {
        var props = this.props;

        //used for hw testing
        var blobsize;
        var sendTime;
        var forceSubmit = false;
        var cameraStartOnLoad = (typeof props.camOnLoad === "undefined") ?
                        false : props.camOnLoad == "true";
        var cameraStarted = false;
        var startRecordButtonExists = (typeof props.recButtonID !== "undefined");
        var shouldAutoRecord = (typeof props.autoRecord === "undefined") ?
                        !startRecordButtonExists : props.autoRecord == "true";

        var replay = props.replay == "true";
        var previewElement;
        var recordAudio, recordVideo;


        var minRecordTime = parseInt(props.minRecordTime);
        if(isNaN(minRecordTime)) {
            minRecordTime = this.defaultMinTime;
        }
        var maxRecordTime = parseInt(props.maxRecordTime);
        if(isNaN(maxRecordTime)) {
            maxRecordTime = this.defaultMaxTime;
        }

        if(startRecordButtonExists) {
            var recordButton = document.getElementById(props.recButtonID);
            if(cameraStartOnLoad)
                recordButton.disabled = true;
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
            if(props.siteView == "submission") {
                if(props.minRecordTime != null) {
                    minrecordTimer = setTimeout(function() {
                                    props.setSubmitEnabled(true);
                                    document.getElementById(props.stopButtonID).onclick = stopRecording;
                                }, 1000*minRecordTime);
                }
                if(props.maxRecordTime != null) {
                    maxrecordTimer = setTimeout(function() {
                                    forceSubmit = true;
                                    if(document.getElementById(props.stopButtonID) != null) {
                                        document.getElementById(props.stopButtonID).onclick();
                                    }
                                }, 1000*maxRecordTime);
                }
            } else {
                document.getElementById(props.stopButtonID).onclick = stopRecording;
                document.getElementById(props.stopButtonID).disabled = false;
            }

            if(typeof props.calc !== "undefined") {
                /*document.getElementById("test-rec-text").innerHTML = "&#x1f534;";*/
                document.getElementById("test-rec-text").innerHTML = "<img class='recLight' src=\'images/rec.png\'>";
            }
            else {
                /*document.getElementById("rec-text").innerHTML = "&#x1f534;";*/
                document.getElementById("rec-text").innerHTML = "<img class='recLight' src=\'images/rec.png\'>";
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
                closeStream();
                mediaStream = stream;
                previewElement.src = window.URL.createObjectURL(mediaStream);
                previewElement.removeAttribute("controls");
                previewElement.setAttribute("muted", "muted");
                window.setTimeout(function() {
                    if(previewElement != null)
                        previewElement.play();
                }, 400);

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
            if(props.siteView == "submission") {
                //props.setSubmitEnabled(false);
                if(forceSubmit == false) {
                    /* TODO We want confirm on submit but it blocks the timer so no confirm for now until a solution is found. */
                    /*if(!confirm("Are you sure you want to submit your answer?")) {
                        return;
                    }*/
                }
            }

            if(!shouldAutoRecord){
                recordButton.disabled = false;
            }
            document.getElementById(props.stopButtonID).disabled = true;

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
                /*if(props.siteView == "feedback") {
                    props.setVideo(recordVideo.getBlob());
                }*/

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

                closeStream();

                if(typeof props.calc !== "undefined") {
                    /*document.getElementById("test-rec-text").innerHTML = "&#11093;";*/
                    if(document.getElementById("test-rec-text") != null) {
                        document.getElementById("test-rec-text").innerHTML = "<img class='recLight' src=\'images/notRec.png\'>";
                    }
                }
                else {
                    /*document.getElementById("rec-text").innerHTML = "&#11093;";*/
                    if(document.getElementById("rec-text")) {
                        document.getElementById("rec-text").innerHTML = "<img class='recLight' src=\'images/notRec.png\'>";
                    }
                }
                cameraStarted = false;
            });

            if(props.siteView == "submission") {
                props.endFunc();
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

            //If a httpCallback exists use that for sending the data.
            if(typeof props.httpCallback !== "undefined") {
                props.httpCallback(formData);
                console.log("callback");
            } else {
                //call xhr with full url, data and callback function
                xhr(props.postURL, formData, props.playCallback);
                console.log("defualt");
            }
        }

        /* Function for sending XMLHttpRequests. */
        function xhr(url, data, callback) {
            var request = new XMLHttpRequest();
            request.onreadystatechange = function () {
                if (request.readyState == 4 && request.status == 200) {
                    callback(request.responseText);
                    alert("Your video has been uploaded successfully!");
                    if(props.siteView == "submission") {
                        location.reload(); // Maby not best solution.
                    }
                } else if(request.readyState == 4 && request.status != 200) {
                    if(request.responseText.length < 10) {
                        // Error message should be longer than 10 characters
                        alert("Failed to upload video.");

                    } else if(request.responseText.includes("Exception")) {
                        // Do not print out exception (should not occur....)
                        alert("Failed to upload video.");
                    } else {
                        alert(request.responseText);
                    }
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

            request.open("POST", url,true);
            console.log(recordVideo.getBlob());
            request.send(data);
        }

        /* Showing recording-light on load (not recording) */

        if(typeof props.calc !== "undefined") {
            /*document.getElementById("test-rec-text").innerHTML = "&#11093;";*/
            if(document.getElementById("test-rec-text") != null) {
                document.getElementById("test-rec-text").innerHTML = "<img class='recLight' src=\'images/notRec.png\'>";
            }
        }
        else {
            /*document.getElementById("rec-text").innerHTML = "&#11093;";*/
            if(document.getElementById("rec-text") != null) {
                document.getElementById("rec-text").innerHTML = "<img class='recLight' src=\'images/notRec.png\'>";
            }
        }
    },
    componentWillUnmount: function () {
        closeStream();
        if(typeof this.props.calc !== "undefined" && typeof $("#you-id")[0] !== "undefined") {
            $("#you-id")[0].pause();
        }
        clearTimeout(minrecordTimer);
        clearTimeout(maxrecordTimer);
    },
    render: function() {
        var id;
        var pId;
        var contId;
      
        if(typeof this.props.contID === "undefined"){
            contId = "prev-container";
        }
        else {
            contId = this.props.contID;
        }
        if(typeof this.props.calc !== "undefined") {
            id="prev-test";
            pId="test-rec-text";
            
        }
        else {
            id="preview";
            pId="rec-text";
            
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
