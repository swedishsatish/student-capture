/**
 * Props params:
 *
 * playCallback:    Function to be called with response from server. should take one parameter.
 * calc:            Only used when testing hardware.
 * postURL:         The url after the globally set url.
 * formDataBuilder  Function to create formdata containing information to send. Function takes:.
 *                                                                                     video(blob) as first param
 *                                                                                     filename as second param.
 * recButtonID:     [Optional] Button to start recording video. If left out it autorecords on render.
 * stopButtonID:    Id of the button to stop recording video. Might post video as well(read about postButtonID).
 * postButtonID:    [Optional] Id of the post video button. If left out the stop button posts the video to server.
 * replay:          replay="true" if you want the recording to be playable in the same video tag.
 * fileName:        The name the video recording should have.
 *
 *
 * watch HardwareTest.js for example of use.
 */



var Recorder = React.createClass({
  componentDidMount: function() {


      var props = this.props;

      //used for hw testing
      var blobsize;
      var sendTime;


      var autoRec = typeof props.recButtonID === "undefined";
      var replay = props.replay == "true";
      function PostBlob(blob) {
          // FormData
          var formData = props.formDataBuilder(blob,props.fileName);

          //used for hw testing
          if(typeof props.calc !== "undefined") {
              blobsize = blob.size / 1048576;
              sendTime = Date.now();
          }

          //call xhr with full url, data and callback function
          xhr(window.globalURL + props.postURL, formData, props.playCallback);
      }


      if(!autoRec){

          var record = document.getElementById(props.recButtonID);
      }

      var stop = document.getElementById(props.stopButtonID);


      var preview = document.getElementById('preview');

      //check webbrowse.
      //var isFirefox = !!navigator.mediaDevices.getUserMedia;

      navigator.getUserMedia = ( navigator.getUserMedia ||
                                  navigator.webkitGetUserMedia ||
      navigator.mediaDevices.getUserMedia ||
                                  navigator.msGetUserMedia);


      var recordAudio, recordVideo;
      var startRecord = function () {
          if(!autoRec){
              record.disabled = true;
          }
          //Start recording with forced low settings for smaller files.
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
              //start stream to record
              preview.src = window.URL.createObjectURL(stream);
              preview.play();


              recordAudio = RecordRTC(stream, {

                  onAudioProcessStarted: function () {
                      //if (!isFirefox) {
                          recordVideo.startRecording();
                      //}
                  }
              });

             // if (isFirefox) {
               //   recordAudio.startRecording();
              //}

              //if (!isFirefox) {
                  recordVideo = RecordRTC(stream, {
                      type: 'video'
                  });
                  recordAudio.startRecording();
              //}

              stop.disabled = false;
          }, function (error) {
              alert("Problem occured, make sure camera is not\nused elsewhere and that you are connected\nby https.");
          });
      };

      if(!autoRec){
          record.onclick = startRecord;
      }
      stop.onclick = function () {
          if(!autoRec){
              record.disabled = false;
          }
          
          stop.disabled = true;

          preview.src = '';
          
          var postbutton = null;
          if (typeof props.postButtonID !== "undefined"){
              postbutton = document.getElementById(props.postButtonID);
          }


        //  if (!isFirefox) {

              recordVideo.stopRecording(function (url) {
                  if(replay){
                      preview.src = url;
                      preview.setAttribute("controls","controls");
                      preview.removeAttribute("muted");
                  }


                  if(postbutton == null) {
                      PostBlob(recordVideo.getBlob());
                  }
                  else {
                      preview.src = url;
                      preview.setAttribute("controls","controls");
                      preview.removeAttribute("muted");
                      postbutton.disabled = false;
                      postbutton.onclick = function () {
                          
                          PostBlob(recordVideo.getBlob());
                          

                      }
                  }
              });
          /*}else {

              recordAudio.stopRecording(function (url) {
                  if(replay){
                      preview.src = url;
                      preview.setAttribute("controls","controls");
                  }
                  if(postbutton == null) {

                      PostBlob(recordAudio.getBlob());
                  }
                  else {
                      preview.src = url;
                      preview.setAttribute("controls","controls");
                      postbutton.disabled = false;
                      postbutton.onclick = function () {
                         
                          PostBlob(recordAudio.getBlob());


                      }
                  }
              });
          }*/

      };



      function xhr(url, data, callback) {
          var request = new XMLHttpRequest();
          request.onreadystatechange = function () {
              if (request.readyState == 4 && request.status == 200) {
                  callback(request.responseText);
              }
          };

          if(typeof props.calc !== "undefined") {
            request.upload.onloadstart = function () {
                $("#internet-speed").text("Uploading...");
            }
              request.onloadstart = function () {
                  $("#internet-speed").text("Uploading...");
              }
          }
          request.onload = function(){
              if(typeof props.calc !== "undefined"){
                  if(request.status == 404)
                      $("#internet-speed").text("Upload failed, no server connection.");
                  else if(request.status == 408)
                      $("#internet-speed").text("Connection timed out.");
                  else
                    props.calc(blobsize,sendTime);
              }
              else if(request.status == 404) {

                      alert("Upload failed, no server connection.");
              }
              else if(request.status == 408) {

                  alert("Connection timed out.");
              }


          }

          request.open('POST', url,true);

          request.send(data);

      }



      if(autoRec){
          startRecord();
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