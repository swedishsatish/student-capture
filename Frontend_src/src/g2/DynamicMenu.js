/**
 * Created by Ludvig on 2016-05-10.
 */


var objToList = function (obj) {
    var res = Object.keys(obj).map(function(key){
        return obj[key];
    });
    return res;
}


var Assignment = React.createClass({

    handleClick: function(assignment,event) {

        var assID = this.props.assignment.assignment.assignmentId;
        var courseID = this.props.courseId;

        if(this.props.role == "student"){

            ReactDOM.render(<AssignmentContent course={courseID} assignment={assID}/>,
                document.getElementById('courseContent'));
        }
        else if(this.props.role == "teacher"){
            $.get(window.globalURL + "/DB/getAllSubmissions",{assignmentID:assID},function (res) {


                ReactDOM.render(<StudentList students={res} courseId={courseID} assignmentId={assID}/>,
                                document.getElementById('courseContent') );
            });

        }


    },
    render: function (){
        var assignment = this.props.assignment;
        var classname = "assignment menuItem navigationText";

        classname += " active";

        return <li className={classname}><div onClick={this.handleClick.bind(this,assignment)}>{assignment.assignment.title}</div></li>;
    }
});
var Assignments = React.createClass({
    handleClick: function (course, event) {
        ReactDOM.render(<NewAssignment courseID={course.courseId} courseCode={course.courseCode} />,document.getElementById("courseContent"))
    },
    render: function() {
        var course = this.props.course;
        var role = this.props.role;
        var assignments = objToList(course.assignments);
        var assList = assignments.map(function (ass) {
            return <Assignment key={ass.assignment.assignmentId} courseId={course.course.courseId} assignment={ass} role={role}/>
        });
        assList.push(<li className="active course menuItem navigationText">
                        <div onClick={this.handleClick.bind(this,course.course)}>
                            + New Assignent
                        </div>
                    </li>
        );
        console.log(assList);
        return <ul>{assList}</ul>;
    }
});
var Course = React.createClass({
    getInitialState : function() {
        return { showChildren : false };
    },
    handleClick: function(course,event) {

            this.setState({showChildren:!this.state.showChildren});


    },
    render: function (){
        var course = this.props.course;
        var classname = "course menuItem navigationText";
        var assignments = "";
        if(this.state.showChildren){
            assignments = <Assignments course={course} role={this.props.role}/>;
        }

        classname += " active";

        return <li className={classname}><div onClick={this.handleClick.bind(this,course)}>{course.course.courseName}</div>{assignments}</li>
    }
});



var DynamicMenu = React.createClass({
    handleClick: function (userID,event) {
        ReactDOM.render(<CreateCourse uid={userID}/>,document.getElementById("courseContent"));
    },

    render: function () {


        var teach;
        var stud;
        if(this.props.tList.length > 0){
            var tList = this.props.tList.map(function(tCourse){
                return <Course key={tCourse.course.courseId} course={tCourse} role="teacher"/>
            });
            teach = <div>
                        <h3>Teacher:</h3>
                        <ul>
                        {tList}
                        </ul>
                    </div>
        }
        if(this.props.sList.length > 0){
            var sList = this.props.sList.map(function(sCourse){
                return <Course key={sCourse.course.courseId} course={sCourse} role="student"/>
            });
            stud = <div>
                        <h3>Student:</h3>
                        <ul>
                            {sList}
                        </ul>
                    </div>
        }


        return (
            <div>
                {teach}
                {stud}
                <br/>
                <ul>
                    <li className="active course menuItem navigationText">
                        <div onClick={this.handleClick.bind(this,this.props.uid)}>
                            + New Course
                        </div>
                    </li>
                </ul>
            </div>
        );
    }
});

$.get(window.globalURL + "/DB/getHierarchy", {userID: 1}, function (res) {
    console.log(res);

    var SCList = objToList(res.studentCourses);
    var TCList = objToList(res.teacherCourses);
    var name = res.firstName + " " + res.lastName;
    ReactDOM.render(<DynamicMenu tList={TCList} sList={SCList} uid={res.userId}/>, document.getElementById("desktopNavigation"));
    ReactDOM.render(<NewProfile name={name}/>, document.getElementById('desktopHeader'));
});