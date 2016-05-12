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
        var formValues = {};
        formValues["studentID"] = $("#student").val();
        formValues["assignmentID"] = $("#ass").val();
        formValues["courseID"] = $("#course").val();
        formValues["courseCode"] = 1;

        $.ajax({
            type : "GET",
            url : window.globalURL+"/feedback/get",
            data : formValues,
            timeout : 100000,
            success : function(response) {
                console.log("SUCCESS: ", response);
                var params = $.param(formValues);
                return (
                    ReactDOM.render(
                        <NewFeedback feedbackResponse={response}
                                     sourceInfo={window.globalURL + "/feedback/video?" + params}/>,
                        document.getElementById('courseContent')
                    )
                )
            }, error : function(e) {
                console.log("ERROR: ", e);
            }
        });

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
                    <video width="720" height="460" src={this.props.sourceInfo} preload="auto" controls/>
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