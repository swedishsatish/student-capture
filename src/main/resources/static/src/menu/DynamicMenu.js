/**
 * @author Ludvig Boström, c13lbm
 */

/**
 * Creates a list out of an object containing multiple items.
 * @param obj
 * @returns {Array}
 */
var objToList = function (obj) {
    if(typeof obj !== "undefined"){
        var res = Object.keys(obj).map(function(key){
            return obj[key];
        });
        return res;
    }
    else {
        return [];
    }

}


var Options = React.createClass({

    studentClick: function () {
        var assID = this.props.assignment.assignment.assignmentID;
        var courseID = this.props.courseId;
        var l = new Date();
        var now = l.getTime() - (l.getTimezoneOffset() * 60000);

        var eDate = new Date(this.props.assignment.assignment.assignmentIntervall.endDate.split(' ').join('T')).getTime();
        if(objToList(this.props.assignment.submissions).length == 0 &&
            eDate >= now){

            ReactDOM.render(<AssignmentContent uid={this.props.uid} course={courseID} assignment={assID}/>,
                document.getElementById('courseContent'));
        }
        else {
            ReactDOM.render(<Feedback course={courseID} assignment={assID} user={this.props.uid}/>,
                document.getElementById('courseContent'));
        }


    },
    editClick: function () {
        var assID = this.props.assignment.assignment.assignmentID;
        var courseID = this.props.courseId;
        ReactDOM.render(<NewAssignment edit={true} courseID={courseID} assID={assID} uid={this.props.uid}/>,document.getElementById("courseContent"))
    },
    deleteClick: function () {
        var assID = this.props.assignment.assignment.assignmentID;
        var courseID = this.props.courseId;
        var reqBody = {};

        reqBody["courseID"] = courseID;
        $.ajax({
            type : "DELETE",
            url : "assignments/" + assID + "?courseID=" + courseID,
            timeout : 100000,
            success : function(response) {
                alert("Removed assignment");
            },
            error : function(e) {
                console.log(e);
                alert("Failed to remove assignment. The assignment may have submissions or it doesn't exist");
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    },
    render: function () {
        return (
            <ul>
                <li className="active course menuItem navigationText">
                    <div onClick={this.studentClick}>
                        Student view
                    </div>
                </li>
                <li className="active course menuItem navigationText">
                    <div onClick={this.editClick}>
                        Edit Assignment
                    </div>
                </li>
                <li className="active course menuItem navigationText">
                    <div onClick={this.deleteClick}>
                        Delete Assignment
                    </div>
                </li>
            </ul>
        );
    }
});

/*  Here assignment information is displayed.
 */
var Assignment = React.createClass({

    getInitialState : function() {
        return { showChildren : false };
    },

    /**
     * Render different views depending on user role on course.
     * @param assignment
     * @param event
     */
    handleClick: function(assignment,event) {
        var uid = this.props.uid;
        var assID = this.props.assignment.assignment.assignmentID;
        var courseID = this.props.courseId;
        if(this.props.role == "student"){
            var l = new Date();
            var now = l.getTime() - (l.getTimezoneOffset() * 60000);

            var eDate = new Date(this.props.assignment.assignment.assignmentIntervall.endDate.split(' ').join('T')).getTime();
            if(objToList(this.props.assignment.submissions).length == 0 &&
                eDate >= now){
                ReactDOM.render(<AssignmentContent uid={uid} course={courseID} assignment={assID}/>,
                    document.getElementById('courseContent'));
            }
            else {
                ReactDOM.render(<Feedback course={courseID} assignment={assID} user={uid}/>,
                    document.getElementById('courseContent'));
            }

        }
        else if(this.props.role == "teacher"){
            var scale = this.props.assignment.assignment.scale;
            document.getElementById("courseContent").innerHTML = "";
            ReactDOM.render(<TeacherViewSubmission courseId={courseID} assignmentId={assID} scale={scale}/>,
                                document.getElementById('courseContent') );
        }
        this.setState({showChildren:!this.state.showChildren});


    },
    /**
     * Render list item for this assigment. Set active if between startdate and enddate.
     * @returns {XML}
     */
    render: function (){
        var assignment = this.props.assignment;
        var classname = "assignment menuItem navigationText";
        var l = new Date();


        var now = l.getTime() - (l.getTimezoneOffset() * 60000);
        var sDate = new Date(assignment.assignment.assignmentIntervall.startDate.split(' ').join('T')).getTime();
        var eDate = new Date(assignment.assignment.assignmentIntervall.endDate.split(' ').join('T')).getTime();


        var options = "";
        if(this.state.showChildren && this.props.role == "teacher"){
            options = <Options assignment={assignment} courseId={this.props.courseId} uid={this.props.uid}/>;
        }
        if(sDate <= now &&
            eDate >= now)
            classname += " active";

        return <li className={classname}><div onClick={this.handleClick.bind(this,assignment)}>{assignment.assignment.title}</div>{options}</li>;
    }
});

var Assignments = React.createClass({
    /**
     * render the new assignment page
     * @param course
     * @param event
     */
    handleClick: function (course, event) {
        ReactDOM.render(<NewAssignment edit={false} courseID={course.courseId} courseCode={course.courseCode} uid={this.props.uid}/>,document.getElementById("courseContent"))
    },
    /**
     * Get the course information and render the edit course page passing course and user id as props.
     * @param course
     * @param event
     */
    editClick: function (course,event) {
        var uid = this.props.uid;
        $.get("course/" + course.courseId,function (res) {
            ReactDOM.render(<EditCourse course={res} uid={uid}/>,document.getElementById("courseContent"));

        });

    },
    /**
     * Create a list of assignments and append teacher specific options for teachers.
     * @returns {XML}
     */
    render: function() {
        var uid = this.props.uid;
        var course = this.props.course;
        var role = this.props.role;
        var assignments = objToList(course.assignments);
        var assList = assignments.map(function (ass) {
            return <Assignment key={ass.assignment.assignmentID} courseId={course.course.courseId} assignment={ass} uid={uid} role={role}/>
        });
        if(role=="teacher"){
            assList.push(<li className="active course menuItem navigationText">
                    <div onClick={this.handleClick.bind(this,course.course)}>
                        + New Assignment
                    </div>
                </li>
            );
            assList.push(<li className="active course menuItem navigationText">
                    <div onClick={this.editClick.bind(this,course.course)}>
                        Edit Course
                    </div>
                </li>
            );
        }


        return <ul>{assList}</ul>;
    }
});
var Course = React.createClass({
    getInitialState : function() {
        return { showChildren : false };
    },
    
    handleClick: function(course,event) {
        var role = this.props.role;
        $.get("course/" + course.course.courseId,function (res) {
            ReactDOM.render(<CourseInfo course={res} role={role}/>,document.getElementById("courseContent"));
            
            
        });

       
    
        
        this.setState({showChildren:!this.state.showChildren});
       

        
    },
    render: function (){
        var course = this.props.course;
        var classname = "course menuItem navigationText";
        var assignments = "";

        if(this.state.showChildren || preload == course.course.courseId){
            assignments = <Assignments course={course} role={this.props.role} uid={this.props.uid}/>;
            this.state.showChildren = true;
            preload = null;
        }


        if(course.course.active)
            classname += " active";

        return <li className={classname}><div onClick={this.handleClick.bind(this,course)}>{course.course.courseName}</div>{assignments}</li>
    }
});


/**
 * Main menu rendering.
 */
var DynamicMenu = React.createClass({
    /**
     * Render createCourse page on clicking the menu item.
     * @param userID
     * @param event
     */
    handleClick: function (userID,event) {
        ReactDOM.render(<CreateCourse uid={userID}/>,document.getElementById("courseContent"));
    },

    searchClick: function (userID,event) {
        ReactDOM.render(<SearchCourse/>,document.getElementById("courseContent"))
    },


    /**
     * Makes separate lists for student/teacher courses. Only gives teachers the option to create course.
     * @returns {XML}
     */
    render: function () {

        var teach;
        var stud;
        var uid = this.props.uid;
        var preload = this.props.preload;
        if(this.props.tList.length > 0){
            var tList = this.props.tList.map(function(tCourse){
                return <Course key={tCourse.course.courseId} course={tCourse} uid={uid} role="teacher"/>
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
                return <Course key={sCourse.course.courseId} course={sCourse} uid={uid} role="student" />
            });
            stud = <div>
                        <h3>Student:</h3>
                        <ul>
                            {sList}
                        </ul>
                    </div>
        }
        var newItem = "";
        if(this.props.isTeacher){
            var newItem =   <li className="active course menuItem navigationText">
                <div onClick={this.handleClick.bind(this,uid)}>
                    + New Course
                </div>
            </li>;
        }

        return (
            <div>
                {teach}
                {stud}
                <br/>
                <ul>
                    {newItem}
                </ul>
            </div>
        );
    }
});

/**
 * Function to get the hierachical structure of the menu
 * and then render the menus.
 *
 */
window.RenderMenu = function (preloaded) {
    $.get("course", function (res) {
        // if(res)
        var userID = res.userId;
        //console.log(res);
        var SCList = objToList(res.studentCourses);
        var TCList = objToList(res.teacherCourses);
        var name = res.firstName + " " + res.lastName;
        preload = preloaded;
        ReactDOM.render(<DynamicMenu tList={TCList} sList={SCList} uid={userID} isTeacher={res.isTeacher}/>, document.getElementById("desktopNavigation"));



        ReactDOM.render(<NewProfile name={name} uid={userID}/>, document.getElementById('desktopHeader'));
    });
};

/**
 * If menu will have a specific course preselected.
 */
var preload;
if(window.getQueryVariable("param") == false){
    RenderMenu();
}
