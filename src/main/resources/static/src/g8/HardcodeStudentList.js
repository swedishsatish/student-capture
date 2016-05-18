/**
 * Created by c13lbm on 4/26/16.
 *
 * This React-component uses Submission.java and SubmissionDAO.java
 */

var HardcodeList = React.createClass({

    submissions: null,
    participants: null,

    // This method is called after the method "render".
    componentDidMount: function () {
        var newTableObject = document.getElementById("students-table");
        sorttable.makeSortable(newTableObject);
        var table11_Props = {
            filters_row_index: 1,
            remember_grid_values: true
        };
        setFilterGrid("students-table", table11_Props);
    },

    // Method that is called when a user clicks on a submission (a table row).
    // It creates a new interface for submission grading..
    clickhandle: function (user, event) {
        console.log(event);
        console.log(user);
        //console.log(event.currentTarget.childNodes[0].innerText)
        /*var user = {
         name: event.currentTarget.childNodes[0].innerText,
         date:event.currentTarget.childNodes[1].innerText,
         grade: event.currentTarget.childNodes[2].innerText,
         }*/
        console.log("Data: " + user.studentName + "_" + user.submissionDate + "_" + user.grade + "_" + user.assignmentID);
        window.studentName = user.studentName;
        window.assignmentID = user.assignmentID;
        window.courseID = user.courseID;
        window.teacherID = user.teacherID;
        //TODO: REMOVE JEBANE
        document.getElementById("courseContent").innerHTML = ""; //TODO: find better solution.
        ReactDOM.render(<RenderHandle />, document.getElementById("courseContent"));
        this.getData();
        //TODO: render other user story.
    },

    getData: function () {

        var reqBody = {};

        reqBody["AssignmentID"] = window.assignmentID;
        reqBody["CourseID"] = window.courseID;
        reqBody["TeacherID"] = window.teacherID;
        reqBody["StudentID"] = window.studentID;

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "https://localhost:8443/feedback/get",
            data: JSON.stringify(reqBody),
            timeout: 100000,
            success: function (response) {
                console.log("SUCCESS: ", response);
                // TODO: check response with if/else, if respons is fail give error message

            }, error: function (e) {
                console.log("ERROR: ", e);
                console.log(reqBody);
            }, done: function (e) {
                console.log("DONE");
            }
        });

    },

    componentWillMount: function () {
        var users = [{
            videoURL:"http://www.w3schools.com/html/mov_bbb.mp4",
            assignmentID: 1000,
            studentID: 21,
            studentName: "Anton Andersson",
            submissionDate: "2010-06-23",
            withdraw: false,
            grade: "G",
            teacherID: 12
        },
            {
                videoURL:"http://techslides.com/demos/sample-videos/small.mp4",
                assignmentID: 1000,
                studentID: 21,
                studentName: "Lukas Lundberg",
                submissionDate: "2010-06-23",
                grade: "K",
                teacherID: 12,
                withdraw: false
            },
            {
                videoURL:"http://easyhtml5video.com/assets/video/new/Penguins_of_Madagascar.mp4",
                assignmentID:213,
                studentID:2,
                studentName: "Daniel Eliasson",
                submissionDate: "2012-05-23",
                grade: "U",
                teacherID: 221,
                withdraw:true
            }
        ];
        window.users=users;
    },

    render: function () {
        var tmp = this;
        console.log("Det är jag som är Elias")

        var userList = window.users.map(function (user) {


            return <tr onClick={tmp.clickhandle.bind(tmp,user)}>
                <video width="96" height="54" class="clip-thumbnail">
                    <source src={user.videoURL} type="video/mp4"/>
                    <source src={user.videoURL} type="video/ogg"/>
                </video>
                <td>{user.studentName}</td>
                <td>{user.submissionDate}</td>
                <td>{user.grade}</td>

            </tr>
        });
        return (
            <div className="row">
                <div className="four columns offset-by-one">
                    <table className="u-full-width sortable" id="students-table">
                        <thead>
                        <tr >

                            <th>Student</th>
                            <th>Date</th>
                            <th>Grade</th>

                        </tr>
                        </thead>
                        <tbody>
                        {userList}
                        </tbody>
                    </table>
                </div>
                <div className="six columns" id="answerContainer">

                </div>

            </div>
        );
    }
});

window.HardcodeList = HardcodeList;