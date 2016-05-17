/**
 * Created by c14hht on 2016-04-28.
 *
 * Class that when entering the student id, assignment id and submitting gets the feedback and view it.
 *
 * This is to be changed when student id, assignment id  will be connected with the logged in person.
 *
 */

window.Feedback = React.createClass({

    handleClick: function () {
        //Skicka till assignments/{assignmentid}/submissions/{studentid}
        $.ajax({
            type: "GET",
            url: window.globalURL + "/assignments/" + $("#assignment").val() + "/submissions/" + $("#student").val(),
            timeout: 100000,
            success: function (response) {
                console.log("SUCCESS: ", response);
                return (
                    ReactDOM.render(
                        <NewFeedback feedbackResponse={response}
                                     sourceInfo={window.globalURL + "/feedback/video?"}/>,
                        document.getElementById('courseContent')
                    )
                )
            }, error: function (e) {
                console.log("ERROR: ", e);
                ReactDOM.render(
                    <h1>
                        ERROR
                    </h1>,
                    document.getElementById('courseContent')
                )
            }, statusCode: {
                404: function () {
                    alert("Page not found");
                }
            }
        });
    },
    render: function () {
        return (
            <div>
                <form>
                    StudentID: <br />
                    <input id="student" type="text" name="studentid"/> <br />
                    AssID: <br />
                    <input id="assignment" type="text" name="assid"/> <br />
                    <button id="submitbutton" type="button" onClick={this.handleClick}>Submit</button>
                </form>
            </div>
        )
    }
});

var NewFeedback = React.createClass({

    render: function () {

        var feedbackResponse = this.props.feedbackResponse;
        return (
            <div>
                <h5>Teacher:</h5>
                {feedbackResponse.teacher}
                <h5>Grade:</h5>
                {feedbackResponse.grade}
                <h5>Time:</h5>
                {feedbackResponse.time}
                <h5>Feedback:</h5>
                {feedbackResponse.feedback}
                <br />
                <video width="720" height="460" src={this.props.sourceInfo} preload="auto" controls/>
            </div>
        )
    }
});