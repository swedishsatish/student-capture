/**
 * Created by c13lbm on 4/26/16.
 *
 * This React-component uses Submission.java and SubmissionDAO.java
 */



var StudentList = React.createClass({

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
        document.getElementById("answerContainer").innerHTML = ""; //TODO: find better solution.
        ReactDOM.render(<RenderHandle />, document.getElementById("answerContainer"));
        //TODO: render other user story.
    },

    render: function () {

        var tmp = this;
        var userList = this.props.students.map(function (user) {
            var date = new Date(user.submissionDate);
            // console.log(tmp);
            return (
                <tr onClick={tmp.clickhandle.bind(tmp,user)}>
                    <td>{user.firstName + " " + user.lastName}</td>
                    <td>{date.getFullYear() + "-" + (date.getMonth()+1/*Months start from 0*/) + "-" + date.getDate()}</td>
                    <td>{user.gradeSign}</td>
                </tr>
            );
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

window.StudentList = StudentList;