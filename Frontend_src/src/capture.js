/**
 * File:	capture.js
 * Author:	Isak Hjelt
 * cs-user:	dv14iht
 * Date:	4/27/16
 *
 * Starts a video and audio stream.
 */

/*Variable for enabling audio and video*/
var constraints = {
 "audio": true,
 "video": true
};

/*The video stream*/
var videoStream;

/*The media frame*/
var gumVideo;


/**
 * Starts a video stream and plays the stream in the specified video frame.
 *
 * @param {string} videoFrame - the name of the video frame to project 
 * 								the stream. Example: video#streamFrame
 */
function startStream(videoFrame){
	/*Get the video frame*/
	gumVideo = document.querySelector(videoFrame);
		
	/*Start the stream*/
	navigator.getUserMedia = navigator.getUserMedia ||
  		navigator.webkitGetUserMedia || navigator.mozGetUserMedia;
  		
  	navigator.getUserMedia(constraints, successCallback, errorCallback);
}

/**
 * Get the video stream (call startStream first).
 * @returns a media stream.
 */
function getStream(){
	return videoStream;
}

function play(){
	gumVideo.play();
}

function pause(){
	gumVideo.pause();
}

function successCallback(stream) {
	console.log('getUserMedia() got stream: ', stream);
  	window.stream = stream;

	if (window.URL) {
		gumVideo.src = window.URL.createObjectURL(stream);
	} else {
		gumVideo.src = stream;
	}
	videoStream = stream;
}

function errorCallback(error) {
	console.log('navigator.getUserMedia error: ', error);
}



