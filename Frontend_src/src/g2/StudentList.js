/**
 * Created by c13lbm on 4/26/16.
 */



var StudentList = React.createClass ({

    componentDidMount: function() {
        var newTableObject = document.getElementById("students-table");
        console.log(1);
        sorttable.makeSortable(newTableObject);
    },

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
        console.log("jebjeb");
        ReactDOM.render(<RenderHandle />,document.getElementById("answerContainer"));
        //TODO: render other user story.
    },

    render: function (){
        
        var tmp = this;
        var userList = this.props.students.map(function (user) {
           // console.log(tmp);
            return <tr onClick={tmp.clickhandle.bind(tmp,user)}>
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