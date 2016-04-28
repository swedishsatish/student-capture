


var recordButton = document.querySelector('button#record');
var stopRecordButton = document.querySelector('button#stopRecord');
var recordedVideo = document.querySelector('video#recorded');




//recordButton.onclick = function() {startRecording(getStream())};
stopRecordButton.onclick = function() {finilize()};


startStream('video#gum');


setTimeout(function(){
    console.log("starting");
    startRecording(getStream());
}, 3000);




function finilize(){
	var theBlob;

	theBlob = stopRecording();
	//recordedVideo.src = window.URL.createObjectURL(theBlob);
	//recordedVideo.controls = true;
	postToServer(theBlob,"user","5DV121","1337");
}
