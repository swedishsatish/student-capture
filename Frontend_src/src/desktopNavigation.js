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
        var assignments = course["assignments"];
        var assignElems = [];
        for(var i = 0; i < assignments.length; i++) {
            assignElems.push(<Assignment key={assignments[i]["_id"]} course={course["_id"]} assignment={assignments[i]} />);
        }
        return <ul>{assignElems}</ul>;
    }
});
var Course = React.createClass({
    getInitialState : function() {
        return { showChildren : false };
    },
    handleClick: function(course,event) {
        if(course["isActive"]) {
            this.setState({showChildren:!this.state.showChildren});
            ReactDOM.render(<CourseContent course={course} type="course" />,
                            document.getElementById('courseContent'));
        }
    },
    render: function (){
        var course = this.props.course;
        var classname = "course menuItem navigationText";
        var assignments = "";
        if(this.state.showChildren){
            assignments = <Assignments course={course} />;
        }
        if(course["isActive"]) {
            classname += " active";
        }
        return <li className={classname}><div onClick={this.handleClick.bind(this,course)}>Course {course["index"]}</div>{assignments}</li>
    }
});
var MenuNav = React.createClass({
    render: function() {
        var courses = JSON.parse(this.props.courses);
        var courseElems = [];
        for(var i = 0; i<courses.length; i++) {
            courseElems.push(<Course key={courses[i]["_id"]} course={courses[i]} />);
        }
        return  <ul>{courseElems}</ul>;
    }
});
getJson("test/test.json",function(courses) {
    ReactDOM.render(<MenuNav courses={courses}/>,document.getElementById("desktopNavigation"));
});
