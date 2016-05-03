


window.AssignmentContent = React.createClass({

    render: function () {

        var assignment = this.props.assignment;
        var course = this.props.course;
        var json;

        getJson("test/assignmentdata.json", function(data) {
            json = JSON.parse(data);
        });




        return (
            <div id="assignment-desc">
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
    getInitialState: function() {
        return {showCountdown: false};
    },
    render: function() {
        return (
            <div>
                { this.state.showCountdown ? <CountDown /> : ''}
                { this.state.showCountdown ? '' : <video id='videoPlayer' width='70%'></video>}
            </div>

        );
    },
    componentDidMount: function() {
        var vid = document.getElementById("videoPlayer");
        vid.src = "http://www.quirksmode.org/html5/videos/big_buck_bunny.mp4"
        //vid.addEventListener('ended',this.onEnded,false);
        vid.play();


    },
    onEnded: function() {
        this.setState({showCountdown: true})
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
