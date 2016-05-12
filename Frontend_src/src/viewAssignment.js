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
        assignmentData.assignmentName = json["AssignmentName"];
        assignmentData.maxTime = json["MaxTime"];
        assignmentData.minTime = json["MinTime"];
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
                    <h5 id="assignment-startAt">Assignment opens: {this.state.startsAt}</h5>
                    <h5 id="assignment-endAt">Assignment closes: {this.state.endsAt}</h5>
                    <h5 id="assignment-mintime">Min video duration: {assignmentData.minTime} seconds</h5>
                    <h5 id="assignment-maxtime">Max video duration: {assignmentData.maxTime} seconds</h5>
                    <h5 id="assignment-information">Assignment information: {this.state.assignmentInformation}</h5>
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
* TODO: Make this popout (modal).
*/
var AssignmentStart = React.createClass({
    getInitialState: function() {
        return {assignmentUrl: '',
                assignmentQuestion: ''
                };
    },
    render: function() {
        return (
            <div  id="assignment-modal">
                <div id="assignment-content" className="modal-content">
                    <h1 id="assignment-title">{assignmentData.assignmentName}</h1>
                    <Vid url={assignmentData.assignmentUrl}/>
                    <BlankBox />
                </div>
            </div>
        )
    }
});

var But = React.createClass({
    getInitialState: function() {
        return {disabled: false};
    },
    onClick: function() {
        if (confirm("Once the assignment starts it cannot be interrupted or paused.\n" +
                    "Are you sure you want to begin the assignment?")) {
            var modal = document.getElementById("assignment-modal");
            modal.style.display = "block";
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
        return (
            <div>
                {content}
            </div>
        );
    }
});

var Question = React.createClass({
    getInitialState: function() {
        return {available: false,
                question: "No question!"
                };
    },
    componentDidMount: function() {
        this.serverRequest = getJson("test/assignmentdata.json", function (data) {
            var json = JSON.parse(data);
            console.log("json is: " + json["AssignmentQuestion"]);
            this.setState({question: json["AssignmentQuestion"], });
        }.bind(this));
    },
    render: function() {
        return (
        <div id="question-div">
            Question: <br/>
            {this.state.question}
        </div>
        );
    }
});

var CountDown = React.createClass({
    getInitialState: function() {
        return {timeLeft: 3,
                startRecord: false
                };
    },
    tick: function() {
        this.setState({timeLeft: this.state.timeLeft - 1});
        if(this.state.timeLeft <= 0) {
            clearInterval(this.interval);
            this.setState({startRecord: true})
        }
    },
    render: function() {
        var content =
            this.state.startRecord
                ? <div id="record-wrap-div">
                      <div id="record-div">
                          <StudentRecordVideo />
                      </div>
                      <div id="question-div">
                          <Question />
                      </div>
                  </div>
                : this.state.timeLeft;
        return (
            <div id="countdown-div">
                {content}
            </div>
        );
    },
    componentDidMount: function() {
        this.interval = setInterval(this.tick, 1000);
    }
});

var Vid = React.createClass({
    getInitialState: function() {
        return {showCountdown: false,
                currTime: 0,
                totalTime: 0,
                loaded: false
                };
    },
    render: function() {
        return (
            <div>
                {this.state.showCountdown
                    ? <CountDown />
                    : <div>
                          <video id='videoPlayer' src={this.props.url} width='70%'></video>
                          <div>
                              <progress value={this.state.currTime} max={this.state.totalTime} />
                              {Math.round(this.state.currTime)} / {Math.round(this.state.totalTime)}
                          </div>
                      </div>
                }
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
    canPlay: function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({totalTime: vid.duration, loaded: true});
    },
    onEnded: function() {
        clearInterval(this.interval);
        this.setState({showCountdown: true})
    },
    onPause : function () {
        var vid = document.getElementById("videoPlayer");
        vid.play();
    },
    ticker: function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({currTime: vid.currentTime});
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
