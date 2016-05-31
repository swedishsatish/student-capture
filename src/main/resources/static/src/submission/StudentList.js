/**
 *@author Ludvig Boström <c13lbm>
 *@revision: Andreas Savva <ens15asa>
 *           Benjamin Björklund <c13bbd>
 *           Tobias Estefors <dv13tes>
 *
 *@note: This class is rendered from TeacherViewSubmission.js
 */

var StudentList = React.createClass({

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
        var student = [{
            "studentID":user.studentID,
            "studentName":user.firstName + " " + user.lastName,
            "courseID":user.courseID,
            "assignmentID":user.assignmentID,
            "teacherName":user.teacherName}];
        document.getElementById("courseContent").innerHTML = "";
        ReactDOM.render(<RenderHandle studentArray={student} idArray={this.props.idArray} scale={this.props.scale} />, document.getElementById("courseContent"));
    },

    componentWillMount: function () {
        this.submissions = this.props.submissions;
        this.participants = this.props.participants;
        console.log(this.participants);
    },

    render: function () {

        var tmp = this;
        var userList = this.submissions.map(function (user) {
            var path = "assignments/" + user.assignmentID + "/submissions/" + user.studentID + "/videos/";
            var studentSubmission = path + 'submission.webm';
            var date = new Date(user.submissionDate);
            return (
                <tr onClick={tmp.clickhandle.bind(tmp,user)}>
                    <video width="96" height="54" class="clip-thumbnail">
                        <source src={studentSubmission} type="video/webm"/>
                        <source src="images/placeholder.webm" type="video/webm"/>
                    </video>
                    <td>{user.firstName + " " + user.lastName}</td>
                    <td>{date.getFullYear() + "-" + (date.getMonth()+1/*Months start from 0)*/ + "-" + date.getDate())}</td>
                    <td>{user.grade.grade}</td>
                </tr>
            );

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