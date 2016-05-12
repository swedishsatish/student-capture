/**
 * Created by c14hht on 2016-04-28.
 *
 * Class that when entering the studentid, assignmentid, courseid and submitting gets the feedback and view it.
 *
 * This is to be changed when studentid, assignmentid and courseid will be connected with the logged in person.
 *
 */

window.Feedback = React.createClass ({

    handleClick: function () {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.onreadystatechange = function() {
            if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                var infoFromDb = JSON.parse(xmlHttp.responseText);
                return (
                    ReactDOM.render(
                        <NewFeedback feedbackResponse={infoFromDb}
                                     sourceInfo={window.globalURL + "/feedback/video?studentID=" + document.getElementById('student').value
                        + "&assignmentID=" + document.getElementById('ass').value + "&courseID=" + document.getElementById('course').value + "&courseCode=1"}/>,
                        document.getElementById('courseContent')
                    )
                )
            }
        };
        xmlHttp.open("GET",window.globalURL + "/feedback/get?studentID=" + document.getElementById('student').value
            + "&assignmentID=" + document.getElementById('ass').value + "&courseID=" + document.getElementById('course').value + "&courseCode=1", true);
        xmlHttp.send();
    },
    render: function () {
        return (
            <div>
                <form>
                    StudentID: <br />
                    <input id="student"type="text" name="studentid"/> <br />
                    AssID: <br />
                    <input id="ass" type="text" name="assid"/> <br />
                    CourseID: <br />
                    <input id="course" type="text" name="courseid"/>
                </form>
                <button style={{backgroundColor: 'black',color:'white'}} onClick={this.handleClick}>Submit</button>
            </div>
        )
    }
});

var NewFeedback = React.createClass ({

    render: function () {

        var feedbackResponse = this.props.feedbackResponse;

        if(feedbackResponse.error === undefined) {
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
                    <video width="720" height="460" src={this.props.sourceInfo} preload="auto" controls></video>
                </div>
            )
        } else {
            return (
                <div>
                    <h1>Error: {feedbackResponse.error}</h1> <br />
                </div>
            )
        }
    }
});