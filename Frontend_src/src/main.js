/*
*  Copyright (c) 2015 The WebRTC project authors. All Rights Reserved.
*
*  Use of this source code is governed by a BSD-style license
*  that can be found in the LICENSE file in the root of the source
*  tree.
*/

'use strict';

/* globals MediaRecorder */

// This code is adapted from
// https://rawgit.com/Miguelao/demos/master/mediarecorder.html

'use strict';

/* globals MediaRecorder */

/*
var mediaSource = new MediaSource();
mediaSource.addEventListener('sourceopen', handleSourceOpen, false);
var mediaRecorder;
var recordedBlobs;
var sourceBuffer;

var gumVideo = document.querySelector('video#gum');
var recordedVideo = document.querySelector('video#recorded');

var recordButton = document.querySelector('button#record');

var downloadButton = document.querySelector('button#download');

var POSTButton = document.querySelector('button#POSTVideo');



recordButton.onclick = toggleRecording;

downloadButton.onclick = download;


POSTButton.onclick = POSTtoserver;



// window.isSecureContext could be used for Chrome
var isSecureOrigin = location.protocol === 'https:' ||
location.host === 'localhost';



// Use old-style gUM to avoid requirement to enable the
// Enable experimental Web Platform features flag in Chrome 49

navigator.getUserMedia = navigator.getUserMedia ||
  navigator.webkitGetUserMedia || navigator.mozGetUserMedia;

var constraints = {
  audio: true,
  video: true
};

navigator.getUserMedia(constraints, successCallback, errorCallback);

function successCallback(stream) {
  console.log('getUserMedia() got stream: ', stream);
  window.stream = stream;
  if (window.URL) {
    gumVideo.src = window.URL.createObjectURL(stream);
  } else {
    gumVideo.src = stream;
  }
}

function errorCallback(error) {
  console.log('navigator.getUserMedia error: ', error);
}

// navigator.mediaDevices.getUserMedia(constraints)
// .then(function(stream) {
//   console.log('getUserMedia() got stream: ', stream);
//   window.stream = stream; // make available to browser console
//   if (window.URL) {
//     gumVideo.src = window.URL.createObjectURL(stream);
//   } else {
//     gumVideo.src = stream;
//   }
// }).catch(function(error) {
//   console.log('navigator.getUserMedia error: ', error);
// });

function handleSourceOpen(event) {
  console.log('MediaSource opened');
  sourceBuffer = mediaSource.addSourceBuffer('video/webm; codecs="vp8"');
  console.log('Source buffer: ', sourceBuffer);
}

function handleDataAvailable(event) {
  if (event.data && event.data.size > 0) {
    recordedBlobs.push(event.data);
  }
}

function handleStop(event) {
  console.log('Recorder stopped: ', event);
}

function toggleRecording() {
  if (recordButton.textContent === 'Start Recording') {
    startRecording();
  } else {
    stopRecording();
    recordButton.textContent = 'Start Recording';

    downloadButton.disabled = false;
  }
}

// The nested try blocks will be simplified when Chrome 47 moves to Stable
function startRecording() {
  var options = {mimeType: 'video/webm'};
  recordedBlobs = [];
  try {
    mediaRecorder = new MediaRecorder(window.stream, options);
  } catch (e0) {
    console.log('Unable to create MediaRecorder with options Object: ', e0);
    try {
      options = {mimeType: 'video/webm,codecs=vp9'};
      mediaRecorder = new MediaRecorder(window.stream, options);
    } catch (e1) {
      console.log('Unable to create MediaRecorder with options Object: ', e1);
      try {
        options = 'video/vp8'; // Chrome 47
        mediaRecorder = new MediaRecorder(window.stream, options);
      } catch (e2) {
        alert('MediaRecorder is not supported by this browser.\n\n' +
            'Try Firefox 29 or later, or Chrome 47 or later, with Enable experimental Web Platform features enabled from chrome://flags.');
        console.error('Exception while creating MediaRecorder:', e2);
        return;
      }
    }
  }
  console.log('Created MediaRecorder', mediaRecorder, 'with options', options);
  recordButton.textContent = 'Stop Recording';

  downloadButton.disabled = true;
  mediaRecorder.onstop = handleStop;
  mediaRecorder.ondataavailable = handleDataAvailable;
  mediaRecorder.start(10); // collect 10ms of data
  console.log('MediaRecorder started', mediaRecorder);
}

function stopRecording() {
  mediaRecorder.stop();
  console.log('Recorded Blobs: ', recordedBlobs);
  recordedVideo.controls = true;
	play();
}

function play() {
  var superBuffer = new Blob(recordedBlobs, {type: 'video/webm'});
  recordedVideo.src = window.URL.createObjectURL(superBuffer);
}

function listVideos(){
	
}

function getVideo() {

    var videoName = document.getElementById('videoname').value;
    
    var url = "https://localhost:8443/videoRequest/";
    var method = "GET";
    
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
    if (xhr.readyState == XMLHttpRequest.DONE) {
        alert(xhr.responseText);
    }
}
    xhr.open(method, url, true);
    xhr.send();


}

function POSTtoserver() {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            var blob = new Blob(recordedBlobs, {type: 'video/webm'});
            var dataPOST = new FormData();

            console.log(xhr.responseText);

            var urlPOST = "https://localhost:8443/uploadVideo/plopp";
            var methodPOST = "POST";


            dataPOST.append("video", blob);
            dataPOST.append("videoName", "video.webm");
            dataPOST.append("userID", "user");

            var xhrPOST = new XMLHttpRequest();

            if ("withCredentials" in xhr) {
                // XHR for Chrome/Firefox/Opera/Safari.
                xhrPOST.open(methodPOST, urlPOST, true);
                //xhr.onreadystatechange = function() {};
                //xhr.open("POST", url, true);
                //Send the proper header information along with the request
                //xhr.setRequestHeader("Content-type", "multipart/form-data; boundary=frontier");
                xhrPOST.send(dataPOST);

            } else if (typeof XDomainRequest != "undefined") {

                // XDomainRequest for IE.
                xhrPOST = new XDomainRequest();
                xhrPOST.open(methodPOST, urlPOST);
                //xhr.open("POST", url, true);
                //Send the proper header information along with the request
                //xhr.setRequestHeader("Content-type", "multipart/form-data; boundary=frontier");

                xhrPOST.send(dataPOST);
            } else {
                // CORS not supported.
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

function download() {
  	var blob = new Blob(recordedBlobs, {type: 'video/webm'});
  	var url = window.URL.createObjectURL(blob);
	
	var oReq = new XMLHttpRequest();

	//var url ="http://localhost:8080/videoRequest";

	oReq.open("POST", url, true);
	

	oReq.send(blob);

	alert("Video uploaded to server!");



  var a = document.createElement('a');
  a.style.display = 'none';
  a.href = url;
  a.download = 'test.webm';
  document.body.appendChild(a);
//  a.click();
  setTimeout(function() {
    //document.body.removeChild(a);
    window.URL.revokeObjectURL(url);
  }, 100);
}

*/

var recordButton = document.querySelector('button#record');
var stopRecordButton = document.querySelector('button#stopRecord');
var recordedVideo = document.querySelector('video#recorded');

recordButton.onclick = function() {startRecording(getStream())};
stopRecordButton.onclick = function() {stopAndPlay()};


startStream('video#gum');

function stopAndPlay(){
	var theBlob;

	theBlob = stopRecording();
	recordedVideo.src = window.URL.createObjectURL(theBlob);
	recordedVideo.controls = true;
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