


var AssignmentContent = React.createClass({

    render: function () {

        var assignment = JSON.parse(this.props.data);


        return (
            <div id="assignment-desc">
                <h1>{assignment["AssignmentName"]}</h1>
                <p>Starts at: {assignment["startsAt"]}
                 <br />Ends at: {assignment["endsAt"]}</p>
                <But />
            </div>
        )
    }
});



var But = React.createClass({
    getInitialState: function() {
        return { showCountdown: false, disabled: false};
    },
    onClick: function() {
        this.setState({ showCountdown: true, disabled: true});

    },
    render: function() {
        return (
            <div>
            <input disabled = {this.state.disabled} type="submit" id="start-video-button" value="Start Assignment" onClick={this.onClick} />
            { this.state.showCountdown ? <CountDown /> : null }
            </div>
        );
    }
});

var CountDown = React.createClass({
    getInitialState: function() {
        return { timeLeft: 5 , showVideo : false};
    },
    tick: function() {
        this.setState({timeLeft : this.state.timeLeft - 1});
        if(this.state.timeLeft <= 0) {
            clearInterval(this.interval);
            this.setState({showVideo : true})
        }

    },
    render: function() {
        return (
            <div id="countdown-div">
            { this.state.showVideo ? <Vid /> : this.state.timeLeft }
            </div>
        );
    },

    componentDidMount: function() {
        this.interval = setInterval(this.tick, 1000);
    }

})
var Vid = React.createClass({
    render: function() {
        return (
            <video id="videoPlayer" width="70%"></video>
        );
    },
    componentDidMount: function() {
        var vid = document.getElementById("videoPlayer");
        vid.src = "http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4"
        vid.play();
    }
});

function getJson(URL, callback) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(JSON.parse(JSON.stringify(xmlHttp.responseText)));
    }
    xmlHttp.open("GET", URL, true);
    xmlHttp.send();
}

getJson("test/assignmentdata.json", function(data) {
    ReactDOM.render(<AssignmentContent data={data} />, document.getElementById('courseContent'));
});
