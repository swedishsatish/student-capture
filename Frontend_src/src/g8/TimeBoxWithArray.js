window.TimeBoxWithArray = React.createClass({

    startTime: null,
    endTime: null,
    intervalId: null,
    err: null,
    stat: null,

    /* This function will automatically run when the class is rendered*/
    getInitialState: function() {
        return {data: '', id: 1, content: 'Ben'};
    },

    /* This function will automatically run after the class has been rendered*/
    componentDidMount: function() {
        this.intervalId = setInterval(this.update, 1000);
    },
    /* This function will automatically run before the class has been rendered*/
    componentWillMount: function () {
       this.getInitTime();
    },
    getInitTime: function() {
        $.ajax({
            url: "http://172.23.135.27:8080/assignment/returnArray", // URL to send to
            type: "GET", // Type of http
            dataType: "json", // Type of data
            async: false,
            data: JSON.stringify({
                courseID: this.props.courseID,
                assignmentID: this.props.assignmentID
            }),
            contentType: "application/json; charset=utf-8", // Also type of data
            success: function(data,status) { // Function to perform when ok
                //Should retrieve an array containing [course ID, assignment title,
                // opening datetime, closing datetime, minimum video time, maximum video time, description]
                this.startTime = new Date(parseInt(data[2]));
                this.endTime = new Date(parseInt(data[3]));
                this.stat = status;
            }.bind(this),
            error: function(xhr, status, err) {
                this.error = err.toString();
                this.stat = status;
                this.endTime = -1;
            }.bind(this)
        });
    },
    /* Function to 'poke' on the state so that React will re-render */
    update: function () {
        this.setState({data: ''});
    },
    render: function() {

        if (this.endTime == -1) {
            return (
                <div>
                    <p>
                        Id: {this.state.id} <br />
                        Content: {this.state.content} <br />
                        Starting time: N/A <br />
                        Ending time: N/A <br />
                        Ends in: N/A
                    </p>
                </div>
            );
        }

        var currentTime = Math.floor(new Date().getTime());
        var timeLeft = Math.ceil((this.endTime.getTime() - currentTime)/1000);

        if (currentTime<this.endTime) {
            return (
                <div>
                    <p>
                        Id: {this.state.id} <br />
                        Content: {this.state.content} <br />
                        Starting time: {this.startTime.toString()} <br />
                        Ending time: {this.endTime.toString()} <br />
                        Ends in: {timeLeft}
                    </p>
                </div>
            );
        } else if (currentTime>=this.endTime) {
            clearInterval(this.intervalId);
            return (
                <div>
                    <p> Id: {this.state.id} <br />
                        Content: {this.state.content} <br />
                        Starting time: {this.startTime.toString()} <br />
                        Ending time: {this.endTime.toString()}
                    </p>
                    <h4>Make your time!</h4>
                </div>
            );
        }
    }
});
