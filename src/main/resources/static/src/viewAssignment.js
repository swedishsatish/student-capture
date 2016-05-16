/*
 * viewAssignment.js
 */

var assignmentData = {
}

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
            getJson("../static/test/assignmentdata.json", this.jsonReady);
        }
        return (
            <div id="assignment-div">
                <div id="assignment-desc">
                    <h1 id="assignment-title">{assignmentData.assignmentName}</h1>
                    <h5 id="assignment-startAt">Assignment opens: <p id="descriptor">{this.state.startsAt}</p></h5>
                    <h5 id="assignment-endAt">Assignment closes: <p id="descriptor">{this.state.endsAt}</p></h5>
                    <h5 id="assignment-mintime">Min video duration: <p id="descriptor">{assignmentData.minTime} seconds</p></h5>
                    <h5 id="assignment-maxtime">Max video duration: <p id="descriptor">{assignmentData.maxTime} seconds</p></h5>
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
    },
    componentDidMount: function() {
    }
});

/*
 * Shows the video and everything else.
 * TODO: start recording AFTER countdown!
 */
var AssignmentStart = React.createClass({
    getInitialState: function() {
        return {startCountDown: false,
                startRecording: false
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
                                <StudentRecordVideo />
                               
                              </div>
                            : <div>
                                <StudentRecordVideo />

                              </div>;
        return (
            <div id="assignment-modal">
            <div class="modal-dialog">
                <div id="assignment-content" className="modal-content">
                    <h1 id="assignment-title">{assignmentData.assignmentName}</h1>
                    <div id="question-div">
                        <Vid url={assignmentData.assignmentUrl} count={this.count}/><br />
                        {questionContent}
                    </div>
                    <div id="countdown-div">
                        {countDownContent}
                    </div>
                    <div id="answer-div">
                        {recordContent}
                    </div>

            </div>
                </div>
            </div>
        )
    },
    count: function() {
        this.setState({startCountDown: true});
    },
    record: function() {
        this.setState({startRecording: true});
    }
});

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
//            console.log("json is: " + json["AssignmentQuestion"]);
            this.setState({question: json["AssignmentQuestion"]});
        }.bind(this));
    }
});

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

var Vid = React.createClass({
    getInitialState: function() {
        return {currTime: 0,
                totalTime: 0
                };
    },
    render: function() {
        return (
            <div>
                <video id='videoPlayer' src={this.props.url}></video>
                <div>
                    <progress id="progress-bar" value={this.state.currTime} max={this.state.totalTime} />
                    {Math.round(this.state.currTime)} / {Math.round(this.state.totalTime)}
                </div>
            </div>
        );
    },
    componentDidMount: function() {
        var vid = document.getElementById("videoPlayer");
        vid.addEventListener('ended',this.onEnded,false);
        vid.addEventListener('pause', this.onPause, false);
        vid.addEventListener('canplay', this.canPlay, false);
        vid.play();
        this.interval = setInterval(this.ticker, 1000);
    },
    ticker: function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({currTime: vid.currentTime});
    },
    canPlay: function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({totalTime: vid.duration});
    },
    onPause : function () {
        var vid = document.getElementById("videoPlayer");
        vid.play();
    },
    onEnded: function() {
        var vid = document.getElementById("videoPlayer");
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