/**
  This class is a button that is used to leave a blank answer for a question.
*/
window.BlankBox = React.createClass({

    withdrew: 0, // used for what is rendered
    error: null, // for error message after http-request
    stat: null, // for error message after http-request
    result: false, // result from submission post

    handleClick: function () {
        document.getElementById("assignment-modal").style.display = 'none';
        if (confirm("You will leave a blank for this question")) { // if user really wants to leave blank
        // send an http ajax post
            $.ajax({
                url: window.globalURL + "/emptyAnswer", // URL to send to
                type: "POST", // Type of http
                dataType: "json", // Type of data
                async: false,
                contentType: "application/json; charset=utf-8", // Also type of data
                data: JSON.stringify({ // Data that is sent
                    courseID: 1,
                    assignmentID: 1,
                    studentID: 1,
                    video: "video"
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

        if (this.withdrew == 1) { // if the withdrawal went ok
            return (
                <div>
                    <h3>You withdrew</h3>
                    <p>
                        Status: {this.stat} <br />
                        Result: {this.result}
                    </p>
                </div>
            );
        } else if (this.withdrew == 2) { // if some error happened
            return (
                <div>
                    <h4>Oh no</h4>
                    <p>
                        Status: {this.stat} <br />
                        Error: {this.error} <br />
                    </p>
                </div>
            );
        } else { // the button to render
            return (

                    <button onClick={this.handleClick}>Withdraw</button>

            );
        }
    }
});