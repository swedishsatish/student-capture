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
  render: function() {
    return (
      <div>
        {this.state.username}'s last gist is
        <a href={this.state.lastGistUrl}>here</a>.




          		<h1>Spike - Media</h1>
          		<p><strong>This demo requires Firefox 29 or later, or Chrome 47 or later</	strong></p>

            <video width="320" height="240" controls src="https://storm.cs.umu.se:8443/videoDownload/bugsbunny" type="video/webm" />

            <button onClick={this.handleClick} id="record">Start Recording</button>
            <button id="download" disabled>Upload</button>


      	<button onClick={this.POSTtoserver} id="POSTVideo">POST Video</button>

      	<input type="text" id="videoname" name="Video: " />


      	<button id="GETVideo">GET Video</button>



      <p>Uploaded Videos </p>
        <p>Your browser does not support iframes.</p>


        </div>
    );
  }
});

ReactDOM.render(<UserGist source="https://api.github.com/users/octocat/gists" />, document.getElementById('courseContent'));