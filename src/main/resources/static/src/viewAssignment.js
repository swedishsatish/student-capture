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
    assignmentUrl: ''
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
        assignmentData.minTime = json["MinTime"];
        assignmentData.maxTime = json["MaxTime"];
        assignmentData.assignmentName = json["AssignmentName"];
        assignmentData.assignmentUrl = json["AssignmentUrl"];
        this.setState({loaded: true,
                       startsAt: json["startsAt"],
                       endsAt: json["endsAt"],
                       assignmentInformation: json["AssignmentInformation"]
                       });
    },
    render: function () {
        var assignment = this.props.assignment;
        var course = this.props.course;
        if(!this.state.loaded) {
            getJson("test/assignmentdata.json", this.jsonReady);
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
                    <But /><br />
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
                                  <StudentRecordVideo autoRecord="true" studentID={2} assignmentID={1200} />
                              </div>
                            : <StudentRecordVideo autoRecord="false" studentID={2} assignmentID={1200} />;
        return (
            <div id="assignment-modal">
                <div className="modal-dialog">
                    <div id="assignment-content" className="modal-content">
                        <h2 id="assignment-title">{assignmentData.assignmentName}</h2>
                        <div id="countdown-div">
                            {countDownContent}
                        </div>
                        <div className="row">
                            <div className="six columns">
                                <div id="question-div">
                                    <h3>Question Video</h3>
                                    <Vid url={assignmentData.assignmentUrl} count={this.count}/><br />
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
                                    {recordContent}
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
                      ? <AssignmentStart />
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
        this.serverRequest = getJson("test/assignmentdata.json", function (data) {
            var json = JSON.parse(data);
            this.setState({question: json["AssignmentQuestion"]});
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
                <video id='videoPlayer' src={this.props.url}>
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
        vid.addEventListener('canplay', this.canPlay, false);
        vid.addEventListener('pause', this.onPause, false);
        vid.addEventListener('ended', this.onEnded, false);
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
        vid.removeEventListener('pause', this.onPause, false);
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
