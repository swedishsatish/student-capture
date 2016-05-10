/**
 * Created by c14hht on 2016-04-28.
 *
 * Class that when entering the studentid, assignmentid, courseid and submitting gets the feedback and view it.
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
                        sourceInfo={"https://" + window.globalURL + "/feedback/video?studentID=" + document.getElementById('student').value
            + "&assignmentID=" + document.getElementById('ass').value + "&courseID=" + document.getElementById('course').value}/>,
                        document.getElementById('courseContent')
                    )
                )
            }
        };
        xmlHttp.open("GET", "https://" + window.globalURL + "/feedback/get?studentID=" + document.getElementById('student').value
            + "&assignmentID=" + document.getElementById('ass').value + "&courseID=" + document.getElementById('course').value, true);
        xmlHttp.send();
    },
    render: function () {
        return (
            <div>
                <form>
                    StudentID: <br />
                    <input id="student"type="text" name="studentid"></input> <br />
                    AssID: <br />
                    <input id="ass" type="text" name="assid"></input> <br />
                    CourseID: <br />
                    <input id="course" type="text" name="courseid"></input>
                </form>
                <button style={{backgroundColor: 'black',color:'white'}} onClick={this.handleClick}>Submit</button>
            </div>
        )
    }
});

var NewFeedback = React.createClass ({

    render: function () {

        var feedbackResponse = this.props.feedbackResponse;
        console.log(this.props.sourceInfo);

        return (
            <div>
                Teacher: {feedbackResponse.teacher} <br />
                Grade: {feedbackResponse.grade} <br />
                Time: {feedbackResponse.time} <br />
                <video width="720" height="460" src={this.props.sourceInfo} preload="auto" controls></video>
            </div>
        )
    }
});

/*ReactDOM.render(
    <Feedback />,
    document.getElementById('courseContent')
);
*/

