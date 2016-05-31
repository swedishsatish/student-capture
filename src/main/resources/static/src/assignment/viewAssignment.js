/*
 * viewAssignment.js
 * Authors: dv13zbn
 *          tm08jsn
 *          c13vzs
 *
 * Shows the assignment front page and (when the assignment is started)
 * plays the assignment video followed by a countdown and finally records the
 * student answer.
 */

/* Global variable containing some data about the assignment */
var assignmentData = {
    minTime: '',
    maxTime: '',
    startsAt: '',
    endsAt: '',
    assignmentName: '',
    assignmentID: '',
    courseID: '',
    studentID: ''
}

/*
 * Shows the assignment front page and sets the assignmentData global variable.
 */
window.AssignmentContent = React.createClass({
    getInitialState: function() {
        return {loaded: false,
                assignmentInformation: ''
                };
    },
    jsonReady: function(data) {
        var json = JSON.parse(data);
        assignmentData.minTime = json["videoIntervall"]["minTimeSeconds"];
        assignmentData.maxTime = json["videoIntervall"]["maxTimeSeconds"];
        assignmentData.startsAt = json["assignmentIntervall"]["startDate"];
        assignmentData.endsAt = json["assignmentIntervall"]["endDate"];
        assignmentData.assignmentName = json["title"];
        this.setState({loaded: true,
                       assignmentInformation: json["description"]
                       });
    },
    render: function () {
        assignmentData.assignmentID = this.props.assignment;
        assignmentData.courseID = this.props.course;
        assignmentData.studentID = this.props.uid;
        if(!this.state.loaded) {
            getJson("assignments/" + assignmentData.assignmentID, this.jsonReady);
        }
        return (
            <div id="assignment-div">
                <h1 id="assignment-title">{assignmentData.assignmentName}</h1>
                <div id="assignment-desc" className="row">
                    <div id="assignment-data" className="six columns">
                        <h5>Assignment opens: <p id="descriptor">{assignmentData.startsAt}</p></h5>
                        <h5>Assignment closes: <p id="descriptor">{assignmentData.endsAt}</p></h5>
                        <h5>Minimum answer video duration: <p id="descriptor">{assignmentData.minTime} seconds</p></h5>
                        <h5>Maximum answer video duration: <p id="descriptor">{assignmentData.maxTime} seconds</p></h5> 
                    </div>
                    <div className="six columns">
                        <h5 id="assignment-information">
                            Assignment information:<br />
                            <p id="descriptor" dangerouslySetInnerHTML={{__html: this.state.assignmentInformation}} />
                        </h5>
                    </div>
                </div>
                <div id="assignment-interaction">
                    <div id ="note-div">
                        NOTE: Once the assignment starts it cannot be interrupted or paused,<br />
                        remember to test your hardware before you begin!
                    </div>
                    {this.state.loaded ? <But /> : <div />}
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
                time: 0,
                disabled: false
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
                            ?
                                  <StudentRecordVideo autoRecord="true"
                                  courseID={assignmentData.courseID}
                                  assignmentID={assignmentData.assignmentID}
                                  studentID={assignmentData.studentID}
                                  minRecordTime={assignmentData.minTime}
                                  maxRecordTime={assignmentData.maxTime}
                                  endFunc={this.endAssignment}
                                  />

                            : <StudentRecordVideo autoRecord="false"
                                    courseID={assignmentData.courseID}
                                    assignmentID={assignmentData.assignmentID}
                                    studentID={assignmentData.studentID}
                                    minRecordTime={assignmentData.minTime}
                                    maxRecordTime={assignmentData.maxTime}
                                    endFunc={this.endAssignment} />;
        var content = this.state.disabled
            ? <div></div>
            : <div id="assignment-modal">
                  <div className="modal-dialog">
                      <div id="assignment-content" className="modal-content">
                          <h2 id="assignment-title">{assignmentData.assignmentName}</h2>
                          <div className="row">
                              <div className="six columns">
                                  <div id="question-div">
                                      <h3 id="videoTitle">Question Video</h3>
                                      <Vid count={this.count}/><br />
                                      {questionContent}
                                  </div>
                              </div>
                              <div className="six columns">
                                  <div id="answer-div">
                                      <h3 id="videoTitle">Answer Video</h3>
                                      <div id="countDownContainer">
                                          <div id="countdown-div">
                                              {countDownContent}
                                          </div>
                                          {recordContent}
                                      </div>
                                      <br /> Allowed video length: {assignmentData.minTime}-{assignmentData.maxTime} seconds
                                      <br /> Current video length: {this.state.time} seconds
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
        return (
            <div>
                {content}
            </div>
        )
    },
    endAssignment: function() {
        clearInterval(this.interval);
        this.setState({disabled: true});
    },
    componentWillUnmount: function() {
        clearInterval(this.interval);
    },
    tick: function() {
        if(!this.state.disabled) {
            if(document.getElementById("answer-div") != null) {
                this.setState({time: this.state.time + 1});
            }
        }
    },
    count: function() {
        this.setState({startCountDown: true});
    },
    record: function() {
        if(!this.state.disabled) {
            this.setState({startRecording: true});
            this.interval = setInterval(this.tick, 1000);
        }
    }
});

/*
 * Button for starting the assignment. It requires confirmation after clicking.
 */
var But = React.createClass({
    getInitialState: function() {
        return {disabled: true,
                start: false
                };
    },
    onClick: function() {
        var startDate = new Date(assignmentData.startsAt);
        var endDate = new Date(assignmentData.endsAt);
        var currDate = new Date();
        if ((startDate < currDate) && (currDate < endDate)) {
            if (confirm("Once the assignment starts it cannot be interrupted or paused. " +
                        "Are you sure you want to begin the assignment?")) {
                this.setState({start: true});
            }
        } else {
            alert("The assignment is not open for viewing at this time!");
        }
    },
    render: function() {
        var button = this.state.disabled
            ? <div
                  className="button primary-button SCButtonDisabled"
              >Start Assignment</div>
            : <div
                  className="button primary-button SCButton"
                  onClick={this.onClick}
              >Start Assignment</div>;
        var content = this.state.start
            ? <AssignmentStart />
            : button;
        return (<div>{content}</div>);
    },
    componentDidMount: function() {
        if (this.state.disabled) {
            var startDate = new Date(assignmentData.startsAt);
            var endDate = new Date(assignmentData.endsAt);
            var currDate = new Date();
            if (currDate < endDate) {
                setTimeout(
                    function() {
                        this.setState({disabled: false});
                    }.bind(this), Math.max(startDate - currDate, 0)
                );
            }
        }
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
               <p dangerouslySetInnerHTML={{__html: this.state.question}} />
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
 */
var CountDown = React.createClass({
    getInitialState: function() {
        return {timeLeft: 10,
                visible: true};
    },
    render: function() {
        var content = this.state.visible
            ? <div>
                  <p id="countdown-text">Recording starts in<br /></p>
                  {this.state.timeLeft}
              </div>
            : <div />;
        return <div>{content}</div>;
    },
    componentDidMount: function() {
        this.interval = setInterval(this.tick, 1000);
    },
    tick: function() {
        beep();
        this.setState({timeLeft: this.state.timeLeft - 1});
        if(this.state.timeLeft <= 0) {
            this.setState({timeLeft: '', visible: false});
            clearInterval(this.interval);
            this.props.record();
        }
    },
    componentWillUnmount: function () {
        clearInterval(this.interval);
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
    componentDidMount: function() {
        setState(this.getInitialState());
    },
    render: function() {
        return (
            <div>
                <p id="video-space">
                    <img
                        className="recLight"
                        /* src is a 1x1 px transparent gif, used as placeholder */
                        src="data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs="
                        alt=""
                    />
                </p>
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
    componentWillUnmount: function () {
        clearInterval(this.interval);
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
        if(vid != null) {
            vid.play();
        }
    },
    onEnded: function() {
        var vid = document.getElementById("videoPlayer");
        if(vid != null) {
            this.setState({currTime: Math.ceil(vid.duration)});
            vid.removeEventListener("pause", this.onPause, false);
            vid.pause();
            clearInterval(this.interval);
            this.props.count();
        }
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

/*
 * Generates a beep sound (without requiring an external sound file).
 */
function beep() {
    (new Audio( /* This is a small sound file encoded in base64 */
	    "data:audio/wav;base64,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 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////VEFHAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAU291bmRib3kuZGUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMjAwNGh0dHA6Ly93d3cuc291bmRib3kuZGUAAAAAAAAAACU="
    )).play();
}

