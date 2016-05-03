var Recorder = React.createClass({
  componentDidMount: function() {
      // PostBlob method uses XHR2 and FormData to submit
      // recorded blob to the PHP server
      var blobsize;
      var sendTime;
      function PostBlob(blob, fileType, fileName) {
          // FormData
          var formData = new FormData();
          formData.append("videoName", fileName);
          formData.append("video", blob);

          blobsize = blob.size/1048576;
          sendTime = Date.now();
          // progress-bar


          // POST the Blob using XHR2
          xhr('https://130.239.42.164:8443/video/textTest', formData, function (fName) {
              //CALLBACK FUNCTION, Download video.
              //openInNewTab('uploads/' + fName.replace(/"/g, ''));
              //window.open('uploads/' + fName.replace(/"/g, ''),'_blank');
              //TODO: play returned video-
              //console.log(fName);
             /* var vid = document.getElementById("returnTestVid");
              vid.src = "data:video/webm;base64,"+fName;
              vid.play();*/


              var container = document.getElementById("tst");
              if(container.childNodes.length > 1){
                  container.removeChild(container.childNodes.item(1));
              }

              var mediaElement = document.createElement("video");

              var source = document.createElement('source');
              source.src = "data:video/webm;base64,"+fName;

              source.type = 'video/webm; codecs="vp8, vorbis"';

              mediaElement.setAttribute("width","100%");
              mediaElement.setAttribute("height","100%");
              mediaElement.appendChild(source);
              mediaElement.controls = true;
              container.appendChild(mediaElement)
              mediaElement.play();


          });
      }

      var record = document.getElementById('record');
      var stop = document.getElementById('stop');


      var audio = document.querySelector('audio');

      var recordVideo = document.getElementById('record-video');
      var preview = document.getElementById('preview');

      var container = document.getElementById('container');

      // if you want to record only audio on chrome
      // then simply set "isFirefox=true"
      var isFirefox = !!navigator.mozGetUserMedia;

      var recordAudio, recordVideo;
      record.onclick = function () {
          record.disabled = true;
          navigator.getUserMedia({
              audio: true,
              video: {
                  mandatory: {
                      minWidth: 160,
                      maxWidth: 320,
                      minHeight: 120,
                      maxHeight: 240,
                      minFrameRate: 5,
                      maxFrameRate: 10
                  }
              }
          }, function (stream) {
              preview.src = window.URL.createObjectURL(stream);
              preview.play();

              // var legalBufferValues = [256, 512, 1024, 2048, 4096, 8192, 16384];
              // sample-rates in at least the range 22050 to 96000.
              recordAudio = RecordRTC(stream, {
                  //bufferSize: 16384,
                  //sampleRate: 45000,
                  onAudioProcessStarted: function () {
                      if (!isFirefox) {
                          recordVideo.startRecording();
                      }
                  }
              });

              if (isFirefox) {
                  recordAudio.startRecording();
              }

              if (!isFirefox) {
                  recordVideo = RecordRTC(stream, {
                      type: 'video'
                  });
                  recordAudio.startRecording();
              }

              stop.disabled = false;
          }, function (error) {
              alert(JSON.stringify(error, null, '\t'));
          });
      };


      stop.onclick = function () {
          record.disabled = false;
          stop.disabled = true;

          preview.src = '';


          if (!isFirefox) {
              recordVideo.stopRecording(function () {
                  PostBlob(recordVideo.getBlob(), 'video', 'hej.webm');
              });
          }else {
              recordAudio.stopRecording(function (url) {
                  preview.src = url;
                  PostBlob(recordAudio.getBlob(), 'video', 'hej.webm');
              });
          }


      };



      function xhr(url, data, callback) {
          var request = new XMLHttpRequest();
          request.onreadystatechange = function () {
              if (request.readyState == 4 && request.status == 200) {
                  callback(request.responseText);
              }
          };

          request.upload.onload = function () {

              $("#internet-speed").text(function () {
                  var now = Date.now();
                  var mbsec = (blobsize/((now - sendTime)/1000));
                  //console.log("mbsec " + mbsec + "now = " + now/1000 + "stime" + sendTime/1000);
                  return "Upload speed = "+ mbsec.toFixed(2) + "MB/s"
              });
          }

          request.open('POST', url,true);

          request.send(data);

      }


  },
  render: function() {

    return (
        <div>
            <div>
                <video id="preview" muted height="100%" width="100%" ></video>
            </div>



        </div>
    );
  }
});

window.Recorder = Recorder;