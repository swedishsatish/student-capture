


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
	POSTtoserver(theBlob);
}

function POSTtoserver(blob) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            var dataPOST = new FormData();

            console.log(xhr.responseText);

            var urlPOST = "https://localhost:8443" + xhr.responseText;
            var methodPOST = "POST";

            dataPOST.append("video", blob);
            dataPOST.append("videoName", "video.webm");
            dataPOST.append("userID", "user");

            var xhrPOST = new XMLHttpRequest();

            if ("withCredentials" in xhr) { // Chrome, Firefox, Opera
                xhrPOST.open(methodPOST, urlPOST, true);
                xhrPOST.send(dataPOST);

            } else if (typeof XDomainRequest != "undefined") { // IE
                xhrPOST = new XDomainRequest();
                xhrPOST.open(methodPOST, urlPOST);
                xhrPOST.send(dataPOST);
            } else { //None
                xhrPOST = null;
            }
        }
    }

        var userID = "user";
        var courseID = "5DV121";
        var examID = "1337";

        var url = "https://localhost:8443/video/inrequest?userID=" + userID +"&courseID=" + courseID+ "&examID=" + examID;
        var method = "GET";

        xhr.open(method, url, true);

        xhr.send();

}