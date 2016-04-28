/**
 * File:	record.js
 * Author:	Isak Hjelt
 * cs-user:	dv14iht
 * Date:	4/27/16
 *
 * Records a video and audio stream.
 */

var recordedBlobs;
var mediaRecorder;

/**
 * This function starts a recording of the stream. 
 * Use the stop() function to get the recording as a file.'
 *
 * @param theStream - the media stream to record.
 */
function startRecording(theStream) {




	var chunkSize = 100;
	recordedBlobs = [];
	
	var options = {
		BitsPerSecond : 120000,
		mimeType : 'video/webm'
	}	
	
	try {

		mediaRecorder = new MediaRecorder(theStream, options);

        var logParagraph = document.createElement("P");
        var t = document.createTextNode("du spelas in NU");
        logParagraph.appendChild(t);
        document.body.appendChild(logParagraph);


	} catch (e0) {
		console.log('Unable to create MediaRecorder with options Object: ', e0);
		console.log('\nblabla\n', theStream);
        try {
			options = {mimeType: 'video/webm,codecs=vp9'};
			mediaRecorder = new MediaRecorder(theStream, options);
		} catch (e1) {
			console.log('Unable to create MediaRecorder with options Object: ', e1);
			try {
				options = 'video/vp8'; // Chrome 47
				mediaRecorder = new MediaRecorder(theStream, options);



			} catch (e2) {
				alert('MediaRecorder is not supported by this browser.\n\n' +
				    'Try Firefox 29 or later, or Chrome 47 or later, '+
				    'Enable experimental Web Platform features enabled from chrome://flags.');
				console.error('Exception while creating MediaRecorder:', e2);
				return;
			}
		}
  	}
	console.log('Created MediaRecorder', mediaRecorder, 'with options', options);
	mediaRecorder.onstop = handleStop;
	mediaRecorder.ondataavailable = handleDataAvailable;
	mediaRecorder.start(chunkSize); // collect 10ms of data
	console.log('MediaRecorder started', mediaRecorder);
}

/**
 * Stop the recording
 *
 * @returns the recording, as a webm blob.
 */
function stopRecording() {
  mediaRecorder.stop();
  console.log('Recorded Blobs: ', recordedBlobs);
  
  return new Blob(recordedBlobs, {type: 'video/webm'});
}

/*Funtion to handle all the blob parts of the recording*/
function handleDataAvailable(event) {
  if (event.data && event.data.size > 0) {
    recordedBlobs.push(event.data);
  }
}

/*Handles a stop in the recording*/
function handleStop(event) {
  console.log('Recording stopped!', event);
}
