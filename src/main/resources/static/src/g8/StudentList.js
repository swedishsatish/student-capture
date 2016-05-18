/**
 * A table with all the students who have grades or should be graded
 * @author Ludvig Boström <c13lbm>
 * @revision Tobias Estefors <dv13tes>
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
    clickHandle: function (user,event) {
        console.log(event);
        console.log(user);
        document.getElementById("answerContainer").innerHTML = ""; //TODO: find better solution.
        ReactDOM.render(<RenderHandle student={user.studentName} />,document.getElementById("courseContent"));
    },

    render: function (){

        console.log("[" + this.props.submissions[0] + "]");
        var tmp = this;
        var userList = this.props.submissions.map(function (user) {
            var date = new Date(user.submissionDate);
            console.log("Name: " + user.studentName);
            return <tr onClick={tmp.clickHandle.bind(tmp,user)}>
                <td>{user.firstName + " " + user.lastName}</td>
                <td>{date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate()}</td>
                <td>{user.gradeSign}</td>
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