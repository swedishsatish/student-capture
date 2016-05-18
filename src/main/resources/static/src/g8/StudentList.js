/**
 *@author Ludvig Boström <c13lbm>
 *@revision: Tobias Estefors <dv13tes>
 */

var StudentList = React.createClass({

    submissions: null,
    participants: null,

    // This method is called after the method "render".
    componentDidMount: function () {
        var newTableObject = document.getElementById("students-table");
        console.log(1);
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
        window.studentName = user.firstName + " " + user.lastName;
        window.assignmentID = user.assignmentID;
        window.courseID = user.courseID;
        window.teacherID = user.teacherID;
        //TODO: REMOVE JEBANE
        document.getElementById("courseContent").innerHTML = ""; //TODO: find better solution.
        ReactDOM.render(<RenderHandle />, document.getElementById("courseContent"));
        //   this.getData();
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

        this.submissions = this.props.submissions;
        this.participants = this.props.participants;
    },

    render: function () {
        var tmp = this;
        var userList = this.submissions.map(function (user) {
            var date = new Date(user.submissionDate);
            return (
                <tr onClick={tmp.clickhandle.bind(tmp,user)}>
                    <video width="96" height="54" class="clip-thumbnail"> <source src="http://techslides.com/demos/sample-videos/small.mp4" type="video/mp4"/> </video>
                    <td>{user.firstName + " " + user.lastName}</td>
                    <td>{date.getFullYear() + "-" + (date.getMonth()+1/*Months start from 0)*/ + "-" + date.getDate())}</td>
                    <td>{user.gradeSign}</td>
                </tr>
            );

            // console.log(tmp);
        });

            </tr>
        });
        return (
            <div className="row">
                <div className="four columns">
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

window.StudentList = StudentList;