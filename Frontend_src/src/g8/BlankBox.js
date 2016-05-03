window.BlankBox = React.createClass({

    withdrew: 0,
    error: null,
    stat: null,
    result: false,

    handleClick: function () {
        if (confirm("You will leave a blank for this question")) {
            $.ajax({
                url: "http://172.23.135.27:8080/emptyAnswer", // URL to send to
                type: "POST", // Type of http
                dataType: "json", // Type of data
                async: false,
                contentType: "application/json; charset=utf-8", // Also type of data
                data: JSON.stringify({ // Data that is sent
                    courseID: 1,
                    assignmentID: 1,
                    studentID: 1,
                    video: "spook"
                }),
                success: function (data, status, xhr) { // Function to perform when success
                    this.result = data.result;
                    this.withdrew = 1;
                    this.stat = status;
                }.bind(this),
                error: function (xhr, status, err) {
                    this.withdrew = 2;
                    this.error = err.toString();
                    this.stat = status;
                }.bind(this)
            });
            this.forceUpdate();
        }
    },
    render: function () {

        if (this.withdrew == 1) {
            return (
                <div>
                    <h3>You withdrew</h3>
                    <p>
                        Status: {this.stat} <br />
                        Result: {this.result}
                    </p>
                </div>
            );
        } else if (this.withdrew == 2) {
            return (
                <div>
                    <h4>Oh no</h4>
                    <p>
                        Status: {this.stat} <br />
                        Error: {this.error} <br />
                    </p>
                </div>
            );
        } else {
            return (
                <div>
                    <button onClick={this.handleClick}>Withdraw</button>
                </div>
            );
        }
    }
});