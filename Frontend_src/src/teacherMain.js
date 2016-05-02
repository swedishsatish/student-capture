var recordButton = document.querySelector('button#record');
var postButton = document.querySelector('button#post');
var recordedVideo = document.querySelector('video#recorded');
var theBlob;


recordButton.onclick = function() {


    toggle();
};


postButton.onclick = function () {postToServer(theBlob,"user","5DV151","1337")};

startStream('video#gum');


function finilize(){

	theBlob = stopRecording();

	console.log(theBlob);
	recordFeedback(false);
	play();
}

function play() {

  recordedVideo.src = window.URL.createObjectURL(theBlob);
}

function toggle() {
  if (recordButton.textContent === "Start Recording") {
    if (startRecording(getStream())) {
            recordFeedback(true);
    }
    recordButton.textContent = "Stop Recording";
  } else {

    finilize();
    recordButton.textContent = "Start Recording";
  }
}
