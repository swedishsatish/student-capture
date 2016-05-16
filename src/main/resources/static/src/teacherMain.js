var recordButton = document.querySelector('button#record');
var postButton = document.querySelector('button#post');
var recordedVideo = document.querySelector('video#recorded');
var blob;

recordButton.onclick = function() {
    toggle();
};


postButton.onclick = function () {

    var length = recordedVideo.length;
    if(length == null && length === 0){
        alert("Video length is 0 or non existing");
    }
    postToServer(blob,"user","5DV151","1337",length);
    postToServer(blob,"user","5DV121","1337",length);
    document.getElementById("post").disabled = true;


};
document.getElementById("post").disabled = true;
startStream('video#gum');


function finilize(){

	blob = stopRecording();
	recordFeedback(false);
	try {
	    play(blob);
    } catch (err) {
        return false;
    }
}

function play(blob) {

  recordedVideo.src = window.URL.createObjectURL(blob);
}

function toggle() {

  if (recordButton.textContent === "Start Recording") {
    if (startRecording(getStream())) {
        recordFeedback(true);
        document.getElementById("post").disabled = true;
    }
    recordButton.textContent = "Stop Recording";
  } else {
    document.getElementById("post").disabled = false;
    finilize();
    recordButton.textContent = "Start Recording";

  }
}


