/*
 * viewAssignment.js
 */


window.AssignmentContent = React.createClass({
    jsonReady: function(data) {
        var json = JSON.parse(data);
        document.getElementById("assignment-title").innerHTML = json["AssignmentName"];
        document.getElementById("assignment-startAt").innerHTML = "Starts at " + json["startsAt"];
        document.getElementById("assignment-endAt").innerHTML = "Ends at " + json["endsAt"];
        document.getElementById("videoPlayer").src = json["assignmentUrl"];
    },
    render: function () {

        var assignment = this.props.assignment;
        var course = this.props.course;

        getJson("test/assignmentdata.json", this.jsonReady);

        return (
            <div id="assignment-div">
                <div id="assignment-desc">
                    <h1 id="assignment-title"></h1>
                    <h5 id="assignment-startAt"></h5>
                    <h5 id="assignment-endAt"></h5>
                </div>
                <div id="assignment-interaction">
                    <BlankBox />
                    <But />
                    <Vid />
                </div>
            </div>
        )
    },
    componentDidMount: function() {
    }
});



var But = React.createClass({
    getInitialState: function() {
        return { disabled: false};
    },
    onClick: function() {
        this.setState({disabled: true});
        document.getElementById("videoPlayer").play();
    },
    render: function() {
        return (
            <div>
            <input disabled = {this.state.disabled} type="submit" id="start-video-button" value="Start Assignment" onClick={this.onClick}  />
            </div>
        );
    }
});

var CountDown = React.createClass({
    getInitialState: function() {
        return { timeLeft: 5 , startRecord : false};
    },
    tick: function() {
        this.setState({timeLeft : this.state.timeLeft - 1});
        if(this.state.timeLeft <= 0) {
            clearInterval(this.interval);
            this.setState({startRecord : true})
        }
    },
    render: function() {
        return (
            <div id="countdown-div">
            { this.state.startRecord ? alert("Start recording") : this.state.timeLeft }
            </div>
        );
    },

    componentDidMount: function() {
        this.interval = setInterval(this.tick, 1000);
    }

})
var Vid = React.createClass({
    getInitialState: function() {
        return {showCountdown: false};
    },
    render: function() {
        return (
            <div>
                { this.state.showCountdown ? <CountDown /> : <video id='videoPlayer' width='70%'></video>}
            </div>
        );
    },
    componentDidMount: function() {
        var vid = document.getElementById("videoPlayer");
        vid.addEventListener('ended',this.onEnded,false);
    },
    onEnded: function() {
        this.setState({showCountdown: true})
    }
});

/*
 * 
 */
function getJson(URL, callback) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(JSON.parse(JSON.stringify(xmlHttp.responseText)));
    }
    xmlHttp.open("GET", URL, true);
    xmlHttp.send();
}

