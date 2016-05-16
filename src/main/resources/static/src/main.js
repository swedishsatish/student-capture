var recordButton = document.querySelector('button#record');
var stopRecordButton = document.querySelector('button#stopRecord');
var recordedVideo = document.querySelector('video#recorded');
var userID = "user";
var courseID = "5DV121";
var examID = "1337";
recordButton.onclick = function() {startRecording(getStream())};
stopRecordButton.onclick = function() {stopAndPlay()};


startStream('video#gum');

function stopAndPlay(){
	var theBlob;

	theBlob = stopRecording();
	recordedVideo.src = window.URL.createObjectURL(theBlob);
	recordedVideo.controls = true;
	postToServer(theBlob,"user","5DV121","1337");
}