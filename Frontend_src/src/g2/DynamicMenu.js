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
        if(assignment["isActive"]) {
            ReactDOM.render(<CourseContent course={this.props.course} id={assignment["_id"]} type="assignment" />,
                document.getElementById('courseContent'));
        }
    },
    render: function (){
        var assignment = this.props.assignment;
        var classname = "assignment menuItem navigationText";
        if(assignment["isActive"]) {
            classname += " active";
        }
        return <li className={classname}><div onClick={this.handleClick.bind(this,assignment)}>Assignment {assignment["index"]}</div></li>;
    }
});
var Assignments = React.createClass({
    render: function() {
        var course = this.props.course;
        var assignments = course.assignments;
       /* var assignElems = [];
        for(var i = 0; i < assignments.length; i++) {
            assignElems.push(<Assignment key={assignments[i]["_id"]} course={course["_id"]} assignment={assignments[i]} />);
        }*/
        console.log(assignments);
        return <ul>assignment</ul>;
    }
});
var Course = React.createClass({
    getInitialState : function() {
        return { showChildren : false };
    },
    handleClick: function(course,event) {
        //if(course["isActive"]) {
            this.setState({showChildren:!this.state.showChildren});

            ReactDOM.render(<CourseContent course={course} type="course" />,
                document.getElementById('courseContent'));
        //}
    },
    render: function (){
        var course = this.props.course;
        var classname = "course menuItem navigationText";
        var assignments = "";
        if(this.state.showChildren){
            assignments = <Assignments course={course} />;
        }
        //if(course["isActive"]) {
            classname += " active";
       // }
        return <li className={classname}><div onClick={this.handleClick.bind(this,course)}>{course.course.courseName}</div>{assignments}</li>
    }
});



var DynamicMenu = React.createClass({


    render: function () {

        //courseElems.push(<Course key={courses[i]["_id"]} course={courses[i]} />);
        var tList = this.props.tList.map(function(tCourse){
            return <Course key={tCourse.course.courseId} course={tCourse} />
        });
        var sList = this.props.sList.map(function(sCourse){
            return <Course key={sCourse.course.courseId} course={sCourse} />
        });

        return (
            <div>
                <h3>Teacher:</h3>
                    <ul>
                        {tList}
                    </ul>
                <h3>Student:</h3>
                    <ul>
                        {sList}
                    </ul>
            </div>
        );
    }
});

$.get(window.globalURL + "/DB/getHierarchy", {userID: 1}, function (res) {
    console.log(res.teacherCourses);

    var SCList = objToList(res.studentCourses);
    var TCList = objToList(res.teacherCourses);

    ReactDOM.render(<DynamicMenu tList={TCList} sList={SCList}/>, document.getElementById("desktopNavigation"))
});