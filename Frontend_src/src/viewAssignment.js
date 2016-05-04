/*
 * viewAssignment.js
 */


window.AssignmentContent = React.createClass({
    getInitialState: function() {
        return {assignmentName : '',
                startsAt : '',
                endsAt : '',
                assignmentUrl : '',
                }
    },
    jsonReady: function(data) {
        var json = JSON.parse(data);
        this.setState({assignmentName: json["AssignmentName"], startsAt: json["startsAt"], endsAt: json["endsAt"], assignmentUrl: json["assignmentUrl"]});
    },
    render: function () {

        var assignment = this.props.assignment;
        var course = this.props.course;

        getJson("test/assignmentdata.json", this.jsonReady);

        return (
            <div id="assignment-div">
                <div id="assignment-desc">
                    <h1 id="assignment-title">{this.state.assignmentName}</h1>
                    <h5 id="assignment-startAt">{this.state.startsAt}</h5>
                    <h5 id="assignment-endAt">{this.state.endsAt}</h5>
                </div>
                <div id="assignment-interaction">
                    <BlankBox />
                    <But />
                    <Vid url={this.state.assignmentUrl}/>
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
                { this.state.showCountdown ? <CountDown /> : <video id='videoPlayer' src={this.props.url} width='70%'></video>}
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
