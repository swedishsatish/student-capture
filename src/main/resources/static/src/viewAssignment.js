/*
 * viewAssignment.js
 * Authors: Zacharias Berggren
 *          Joakim Sandman
 *          Victor Zars
 *
 * Shows the assignment front page and (when the assignment is started)
 * plays the assignment video followed by a countdown and finally records the
 * student answer.
 */

/* Global variable containing some data about the assignment */
var assignmentData = {
    minTime: '',
    maxTime: '',
    assignmentName: '',
    assignmentID: ''
}

/*
 * Shows the assignment front page and sets the assignmentData global variable.
 */
window.AssignmentContent = React.createClass({
    getInitialState: function() {
        return {loaded: false,
                startsAt: '',
                endsAt: '',
                assignmentInformation: ''
                };
    },
    jsonReady: function(data) {
        var json = JSON.parse(data);
        assignmentData.minTime = json["videoIntervall"]["minTimeSeconds"];
        assignmentData.maxTime = json["videoIntervall"]["maxTimeSeconds"];
        assignmentData.assignmentName = json["title"];
        this.setState({loaded: true,
                       startsAt: json["assignmentIntervall"]["startDate"],
                       endsAt: json["assignmentIntervall"]["endDate"],
                       assignmentInformation: json["description"]
                       });
    },
    render: function () {
        assignmentData.assignmentID = this.props.assignment;
        if(!this.state.loaded) {
            getJson("assignments/" + assignmentData.assignmentID, this.jsonReady);
        }
        return (
            <div id="assignment-div">
                <div id="assignment-desc">
                    <h1 id="assignment-title">{assignmentData.assignmentName}</h1>
                    <h5 id="assignment-startAt">Assignment opens: <p id="descriptor">{this.state.startsAt}</p></h5>
                    <h5 id="assignment-endAt">Assignment closes: <p id="descriptor">{this.state.endsAt}</p></h5>
                    <h5 id="assignment-mintime">Minimum answer video duration: <p id="descriptor">{assignmentData.minTime} seconds</p></h5>
                    <h5 id="assignment-maxtime">Maximum answer video duration: <p id="descriptor">{assignmentData.maxTime} seconds</p></h5>
                    <h5 id="assignment-information">
                        Assignment information:<br />
                        <p id="descriptor">{this.state.assignmentInformation}</p>
                    </h5>
                </div>
                <div id="assignment-interaction">
                    <But user={this.props.uid} assignment={this.props
                    .assignment}/><br />
                    NOTE: Once the assignment starts it cannot be interrupted or paused,<br />
                    remember to test your hardware before you begin!
                </div>
            </div>
        )
    }
});

/*
 * Shows the video, question summary, countdown and student recording video
 * in the appropriate order.
 */
var AssignmentStart = React.createClass({
    getInitialState: function() {
        return {startCountDown: false,
                startRecording: false,
                time: 0
                };
    },
    render: function() {
        var questionContent = this.state.startCountDown
                              ? <Question />
                              : <div />;
        var countDownContent = this.state.startCountDown
                               ? <CountDown record={this.record} />
                               : <div />;
        var recordContent = this.state.startRecording
                            ? <div>
                                  <StudentRecordVideo autoRecord="true"
                                  studentID={this.props.user}
                                  assignmentID={this.props.assignment} />
                              </div>
                            : <StudentRecordVideo autoRecord="false"
                            studentID={this.props.user} assignmentID={this
                            .props.assignment} />;
        return (
            <div id="assignment-modal">
                <div className="modal-dialog">
                    <div id="assignment-content" className="modal-content">
                        <h2 id="assignment-title">{assignmentData.assignmentName}</h2>
                        <div className="row">
                            <div className="six columns">
                                <div id="question-div">
                                    <h3>Question Video</h3>
                                    <Vid count={this.count}/><br />
                                    {questionContent}
                                </div>
                            </div>
                            <div className="six columns">
                                <div id="answer-div">
                                    <h3 id="videoTitle">Answer Video</h3>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <svg width="30" height="30">
                                        <circle cx="15" cy="15" r="11"
                                        stroke="black" stroke-width="4"
                                        fill="white" id="recCircle" />
                                        Rec circle.
                                    </svg>
                                    <p id="descriptor">[REC]</p>
                                    <div id="countDownContainer">
                                        <div id="countdown-div">
                                            {countDownContent}
                                        </div>
                                        {recordContent}
                                    </div>
                                    <br /> Allowed video length: {assignmentData.minTime}-{assignmentData.maxTime}
                                    <br /> Current video length: {this.state.time}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        )
    },
    tick: function() {
        this.setState({time: this.state.time + 1});
    },
    count: function() {
        this.setState({startCountDown: true});
    },
    record: function() {
        var rec = document.getElementById("recCircle");
        rec.style.fill = "red";
        this.setState({startRecording: true});
        this.interval = setInterval(this.tick, 1000);
    }
});

/*
 * Button for starting the assignment. It requires confirmation after clicking.
 */
var But = React.createClass({
    getInitialState: function() {
        return {disabled: false};
    },
    onClick: function() {
        if (confirm("Once the assignment starts it cannot be interrupted or paused.\n" +
                    "Are you sure you want to begin the assignment?")) {
            this.setState({disabled: true});
        }
    },
    render: function() {
        var content = this.state.disabled
                      ? <AssignmentStart user={this.props.user}
                      assignment={this.props.assignment} />
                      : <input
                            disabled = {this.state.disabled}
                            type="submit"
                            id="start-video-button"
                            value="Start Assignment"
                            onClick={this.onClick} />;
        return (<div>{content}</div>);
    }
});

/*
 * Shows question summary after retrieving it from the json file.
 */
var Question = React.createClass({
    getInitialState: function() {
        return {question: ''};
    },
    render: function() {
        return (
            <div>
                Question summary: <br/>
                {this.state.question}
            </div>
        );
    },
    componentDidMount: function() {
        this.serverRequest = getJson("assignments/" + assignmentData.assignmentID, function (data) {
            var json = JSON.parse(data);
            this.setState({question: json["recap"]});
        }.bind(this));
    }
});

/*
 * Shows the countdown and then signal parent to start recording.
 * TODO: Set countdown time to 10 (or read from json).
 */
var CountDown = React.createClass({
    getInitialState: function() {
        return {timeLeft: 3};
    },
    render: function() {
        return <div>{this.state.timeLeft}</div>;
    },
    componentDidMount: function() {
        this.interval = setInterval(this.tick, 1000);
    },
    tick: function() {
        this.setState({timeLeft: this.state.timeLeft - 1});
        if(this.state.timeLeft <= 0) {
            this.setState({timeLeft: ''});
            clearInterval(this.interval);
            this.props.record();
        }
    }
});

/*
 * Shows the assignment video (which autoplays), progress bar and time passed.
 * Then signals parent to start countdown and show question summary.
 * TODO: fix infinity for short video.
 */
var Vid = React.createClass({
    getInitialState: function() {
        return {currTime: 0,
                totalTime: 0
                };
    },
    render: function() {
        return (
            <div>
                <video id="videoPlayer" src={"assignments/" + assignmentData.assignmentID + "/video"}>
                    Cannot show video, it may not be supported by your browser!
                </video>
                <div className="row">
                    <div className="eight columns">
                        <progress id="progress-bar" value={this.state.currTime} max={this.state.totalTime} />
                    </div>
                    <div className="four columns">
                        {this.state.currTime} / {this.state.totalTime}
                    </div>
                </div>
            </div>
        );
    },
    componentDidMount: function() {
        var vid = document.getElementById("videoPlayer");
        vid.oncontextmenu = function (e) {e.preventDefault();};
        vid.addEventListener("canplay", this.canPlay, false);
        vid.addEventListener("pause", this.onPause, false);
        vid.addEventListener("ended", this.onEnded, false);
        vid.play();
        this.interval = setInterval(this.ticker, 1000);
    },
    ticker: function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({currTime: Math.ceil(vid.currentTime)});
    },
    canPlay: function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({totalTime: Math.ceil(vid.duration)});
    },
    onPause : function () {
        var vid = document.getElementById("videoPlayer");
        vid.play();
    },
    onEnded: function() {
        var vid = document.getElementById("videoPlayer");
        this.setState({currTime: Math.ceil(vid.duration)});
        vid.removeEventListener("pause", this.onPause, false);
        vid.pause();
        clearInterval(this.interval);
        this.props.count();
    }
});

/*
 * Gets json code from the URL and then calls the callback function.
 */
function getJson(URL, callback) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            callback(JSON.parse(JSON.stringify(xmlHttp.responseText)));
        }
    }
    xmlHttp.open("GET", URL, true);
    xmlHttp.send();
}

