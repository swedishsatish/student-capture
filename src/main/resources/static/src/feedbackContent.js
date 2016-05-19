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
                var obj = JSON.parse(e.responseText);
                console.log(obj);
                ReactDOM.render(
                    <div>
                        <h3>Status: {obj.status}</h3>
                        <h3>Error: {obj.error}</h3>
                        <h3>Message: {obj.message}</h3>
                    </div>,
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

        feedbackResponse.submissionDate = new Date((feedbackResponse
        .submissionDate));

        var gradeColor;

        if(feedbackResponse.grade.grade === 'MVG') {
            gradeColor = 'green';
        } else if(feedbackResponse.grade.grade === 'IG') {
            gradeColor = 'red';
        }

        return (
            <div>
                <h5 style={{color:gradeColor}}>Grade: {feedbackResponse.grade
                .grade}</h5>
                <h5>Submissiondate: {feedbackResponse.submissionDate.toGMTString()}</h5>
                <h5>Feedback: {feedbackResponse.feedback}</h5>
                <h5>Teachername: {feedbackResponse.teacherName}</h5>
                <br />
                <video width="720" height="460" src={this.props.sourceInfo} preload="auto" controls/>
            </div>
        )
    }
});