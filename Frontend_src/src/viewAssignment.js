/*
 * viewAssignment.js
 */

var assignmentData = {
}
window.AssignmentContent = React.createClass({
    getInitialState: function() {
        return {loaded : false,
                assignmentName : '',
                startsAt : '',
                endsAt : '',
                assignmentUrl : '',
                }
    },
    jsonReady: function(data) {
        var json = JSON.parse(data);
        this.setState({loaded: true, assignmentName: json["AssignmentName"], startsAt: json["startsAt"], endsAt: json["endsAt"], assignmentUrl: json["assignmentUrl"]});
        assignmentData.assignmentUrl = json["assignmentUrl"];
    },
    render: function () {

        var assignment = this.props.assignment;
        var course = this.props.course;
        if(!this.state.loaded)
            getJson("test/assignmentdata.json", this.jsonReady);


        return (
            <div id="assignment-div">
                <div id="assignment-desc">
                    <h1 id="assignment-title">{this.state.assignmentName}</h1>
                    <h5 id="assignment-startAt">{this.state.startsAt}</h5>
                    <h5 id="assignment-endAt">{this.state.endsAt}</h5>
                </div>
                <div id="assignment-interaction">
                    <But />
                </div>
            </div>
        )
    },
    componentDidMount: function() {

    }
});


var AssignmentStart = React.createClass({
    getInitialState: function() {
        return {
                    assignmentUrl : '',
                    assignmentDescription: ''
                }
    },
    render: function() {
        return (
            <div>
                <Vid url={assignmentData.assignmentUrl}/>
                <BlankBox />
            </div>
        )
    }
})


var But = React.createClass({
    getInitialState: function() {
        return { disabled: false};
    },
    onClick: function() {
        this.setState({disabled: true});
    },
    render: function() {
        var content = this.state.disabled ? <AssignmentStart /> : <input disabled = {this.state.disabled} type="submit" id="start-video-button" value="Start Assignment" onClick={this.onClick}  /> ;
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
                question: ''};
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
})

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
        var content = this.state.startRecord ? <div id="record-wrap-div">
                                                    <div id="record-div">
                                                        <StudentRecordVideo />
                                                    </div>
                                                    <div id="question-div">
                                                        <Question />
                                                    </div>
                                                </div> : this.state.timeLeft;
        return (
            <div id="countdown-div">
            { content }
            </div>
        );
    },

    componentDidMount: function() {
        this.interval = setInterval(this.tick, 1000);
    }

})
var Vid = React.createClass({
    getInitialState: function() {
        return {showCountdown: false,
                currTime: 0,
                totalTime: 0,
                };
    },
    render: function() {
        return (
            <div>
                { this.state.showCountdown ? <CountDown /> : <div><video id='videoPlayer' src={this.props.url} width='70%'></video>
                                                             <div>
                                                                 {Math.round(this.state.currTime)} / {Math.round(this.state.totalTime)}
                                                             </div></div>}
            </div>
        );
    },
    componentDidMount: function() {
        var vid = document.getElementById("videoPlayer");
        vid.addEventListener('ended',this.onEnded,false);
        vid.addEventListener('playing',this.onStarted,false);
        vid.addEventListener('paused', this.onPause, false);
        vid.play();
        this.interval = setInterval(this.ticker, 1000);
    },
    onEnded: function() {
        console.log("video ended");
        clearInterval(this.interval);
        this.setState({showCountdown: true})
    },
    onStarted : function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({totalTime: vid.duration});
    },
    onPause : function () {
        var vid = document.getElementById("videoPlayer");

    },
    ticker: function () {
        var vid = document.getElementById("videoPlayer");
        this.setState({currTime: vid.currentTime});
        console.log(vid.currentTime);
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
