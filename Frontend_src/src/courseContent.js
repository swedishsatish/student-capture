var UserGist = React.createClass({
  getInitialState: function() {
    return {
      username: '',
      lastGistUrl: ''
    };
  },

  componentDidMount: function() {
    this.serverRequest = $.get(this.props.source, function (result) {
      var lastGist = result[0];
      this.setState({
        username: lastGist.owner.login,
        lastGistUrl: lastGist.html_url
      });
    }.bind(this));

  },

  componentWillUnmount: function() {
    this.serverRequest.abort();
  },
  handleClick: function() {
        alert("Start recording")
    },

  POSTtoserver: function () {
    //var blob = new Blob(recordedBlobs, {type: 'video/webm'});
    var data = new FormData();
    var url = "https://storm.cs.umu.se:8443/uploadVideo";
    //var params = "studentVideo=video";
    var method = "POST";

   // data.append("video", blob);
    data.append("videoName", "video.webm");
    data.append("userID", "Ludvigo1232355");

    var xhr = new XMLHttpRequest();

  if ("withCredentials" in xhr)
  {
    // XHR for Chrome/Firefox/Opera/Safari.
    xhr.open(method, url, true);
    //xhr.onreadystatechange = function() {};
    //xhr.open("POST", url, true);
    //Send the proper header information along with the request
    //xhr.setRequestHeader("Content-type", "multipart/form-data; boundary=frontier");
    xhr.send(data);

  }

  else if (typeof XDomainRequest != "undefined")
  {

    // XDomainRequest for IE.
    xhr = new XDomainRequest();
    xhr.open(method, url);
    //xhr.open("POST", url, true);
    //Send the proper header information along with the request
    //xhr.setRequestHeader("Content-type", "multipart/form-data; boundary=frontier");

    xhr.send(data);
  } else {
    // CORS not supported.
    xhr = null;
  }
},
startRecording: function () {
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
},

handleSourceOpen: function (event) {
  console.log('MediaSource opened');
  sourceBuffer = mediaSource.addSourceBuffer('video/webm; codecs="vp8"');
  console.log('Source buffer: ', sourceBuffer);
},

 handleDataAvailable: function(event) {
  if (event.data && event.data.size > 0) {
    recordedBlobs.push(event.data);
  }
},
 handleStop: function(event) {
  console.log('Recorder stopped: ', event);
},

 toggleRecording: function() {
  if (recordButton.textContent === 'Start Recording') {
    startRecording();
  } else {
    stopRecording();
    recordButton.textContent = 'Start Recording';

    downloadButton.disabled = false;
  }
},

 stopRecording: function() {
  mediaRecorder.stop();
  console.log('Recorded Blobs: ', recordedBlobs);
  recordedVideo.controls = true;
	play();
},

 play: function() {
  var superBuffer = new Blob(recordedBlobs, {type: 'video/webm'});
  recordedVideo.src = window.URL.createObjectURL(superBuffer);
},
  render: function() {

    return (
      <div id="videoDivFrame">

            <iframe name="videoplay" src="teacherVideo.html"></iframe>

            <p><a href="studentVideo.html" target="videoPlay">StudentVideo</a></p>
        </div>
    );
  }
});


ReactDOM.render(<UserGist source="https://api.github.com/users/octocat/gists" />, document.getElementById('courseContent'));