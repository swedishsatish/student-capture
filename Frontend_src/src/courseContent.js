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
  render: function() {

    return (
      <div id="videoDivFrame">
            <iframe name="videoplay" src="teacherVideo.html"></iframe>
        </div>
    );
  }
});

ReactDOM.render(<UserGist source="https://api.github.com/users/octocat/gists" />, document.getElementById('courseContent'));