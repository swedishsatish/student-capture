


var recordButton = document.querySelector('button#record');
var stopRecordButton = document.querySelector('button#stopRecord');
var postButton = document.querySelector('button#post');
var recordedVideo = document.querySelector('video#recorded');
var theBlob;



recordButton.onclick = function() {startRecording(getStream())};
stopRecordButton.onclick = function() {finilize()};
postButton.onclick = function () {postToServer(theBlob,"user","5DV121","1337")};


startStream('video#gum');


function finilize(){
	theBlob = stopRecording();
}
