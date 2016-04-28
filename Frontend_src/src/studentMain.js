


var recordButton = document.querySelector('button#record');
var stopRecordButton = document.querySelector('button#stopRecord');
var recordedVideo = document.querySelector('video#recorded');




//recordButton.onclick = function() {startRecording(getStream())};
stopRecordButton.onclick = function() {finilize()};


startStream('video#gum');


setTimeout(function(){
    console.log("starting");
    if(startRecording(getStream())) {
        recordFeedback(true);
    }
}, 3000);




function finilize(){
	var theBlob;
    console.log("finilize")
	theBlob = stopRecording();
	postToServer(theBlob,"user","5DV121","1337");
	recordFeedback(false);
}
