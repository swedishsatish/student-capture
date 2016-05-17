/**
 * A table with all the students who have grades or should be graded
 * @author c13lbm
 * @revision dv13tes
 */

var StudentList = React.createClass ({

    componentDidMount: function() {
        var newTableObject = document.getElementById("students-table");
        console.log(1);
        sorttable.makeSortable(newTableObject);
        var table11_Props = {
            filters_row_index: 1,
            remember_grid_values: true
        };
        setFilterGrid( "students-table",table11_Props )

    },

    /**
     * Starts the submit feedback when a user is clicked
     * @param user the user who gets feedback
     * @param event
     */
    clickhandle: function (user,event) {
        console.log(event);
        console.log(user);
        //console.log(event.currentTarget.childNodes[0].innerText)
        /*var user = {
         name: event.currentTarget.childNodes[0].innerText,
         date:event.currentTarget.childNodes[1].innerText,
         grade: event.currentTarget.childNodes[2].innerText,
         }*/
        console.log(user.studentName + "_" + user.submissionDate + "_" + user.grade + "_" + user.assignmentID);
        window.studentName=user.studentName;
        window.assignmentID=user.assignmentID;
        window.courseID=user.courseID;
        window.teacherID=user.teacherID;
        document.getElementById("answerContainer").innerHTML = ""; //TODO: find better solution.
        ReactDOM.render(<RenderHandle student={user.studentName} />,document.getElementById("courseContent"));
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


                //document.getElementById('dropDownMenu').value =


                // TODO: check response with if/else, if respons is fail give error message

                //  ReactDOM.render(<div>HEJ</div>, document.getElementById('courseContent'));
            }, error: function (e) {
                console.log("ERROR: ", e);
            }, done: function (e) {
                console.log("DONE");
            }
        });

    },

    render: function (){

        var tmp = this;
        var userList = window.users.map(function (user) {
            console.log(user.videoURL);


            return <tr onClick={tmp.clickhandle.bind(tmp,user)}>
                <video width="96" height="54" class="clip-thumbnail"> <source src={user.videoURL} type="video/mp4"/> </video>
                <td>{user.studentName}</td>
                <td>{user.submissionDate}</td>
                <td>{user.grade}</td>

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