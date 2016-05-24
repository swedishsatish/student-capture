/**
 This class is a button that is used to leave a blank answer for a question.
 @author Tobias Estefors <dv13tes>,
         Benjamin Björklund <c13bbn>
 */
window.BlankBox = React.createClass({

    withdrew: 0, // used for what is rendered
    error: null, // for error message after http-request
    stat: null, // for error message after http-request
    result: false, // result from submission post
    /**
     * Sends a http-post with a blank answer when button is clicked
     */
    handleClick: function () {

        /* TODO Will freeze all timers so no confirm until solution found. */
        //if (confirm("You will leave a blank for this question")) { // if user really wants to leave blank
            // send an http ajax post
            var fd = new FormData();
            fd.append('submission', new Blob([JSON.stringify({ // Data that is sent
                     courseID: this.props.courseID,
                     assignmentID: this.props.assignmentID,
                     studentID: this.props.studentID,
                 })], {
                    type: "application/json"
                 }));

            $.ajax({
                url: this.props.postURL, // URL to send to
                type: "POST", // Type of http
                data: fd,
                processData: false,
                contentType: false,
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
        //}
        var hasModal = document.getElementById("assignment-modal");
        if (hasModal !== null) {
            hasModal.style.display = 'none';
        }
        document.getElementById("studentSubmit").onclick = function() {};
        document.getElementById("recorder").
    },
    /**
     * Renderfunction for studentlist, renders the list on a div
     * @returns {XML}
     */
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
                <div>
                    <button id="withrawbutton" onClick={this.handleClick}>Withdraw</button>
                </div>
            );
        }
    }
});