

var Tasks = React.createClass({
    handleClick: function(task, event) {
        if(task["isActive"])
            ReactDOM.render(<CourseContent id={task["_id"]} type="task" />,
                                document.getElementById('courseContent'));
    },
    render: function() {
        var assignment = this.props.assignment;
        var tasks = assignment["tasks"];
        var taskElems = [];
        for(var i = 0; i < tasks.length; i++) {
            var classname = "task menuItem navigationText";
            if(tasks[i]["isActive"]) {
                classname += " active";
            }
            taskElems.push(<li className={classname} onClick={this.handleClick.bind(this,tasks[i])}>Task {i+1}</li>);
        }
        return <ul>{taskElems}</ul>;
    }
});
var Assignment = React.createClass({
    getInitialState : function() {
        return { showChildren : true };
    },
    handleClick: function(assignment,event) {
        if(this.state.showChildren && assignment["isActive"]) {
            ReactDOM.render(<CourseContent id={assignment["_id"]} type="assignment" />,
                            document.getElementById('courseContent'));
            event.target.nextElementSibling.className="done";
            ReactDOM.render(<Tasks assignment={assignment} />,event.target.nextElementSibling);
        } else {
            event.target.nextElementSibling.className="invisible";
        }
        this.setState({showChildren:!this.state.showChildren});
    },
    render: function (){
        var assignment = this.props.assignment;
        var classname = "assignment menuItem navigationText";
        if(assignment["isActive"]) {
            classname += " active";
        }
        return <li onClick={this.handleClick.bind(this,assignment)} className={classname}>Assignment {assignment["index"]}</li>;
    }
});
var Assignments = React.createClass({
    render: function() {
        var course = this.props.course;
        var assignments = course["assignments"];
        var assignElems = [];
        for(var i = 0; i < assignments.length; i++) {
            assignElems.push(<Assignment assignment={assignments[i]} />);
            assignElems.push(<ul className="invisible"></ul>);            
        }
        return <ul>{assignElems}</ul>;
    }
});
var Course = React.createClass({
    getInitialState : function() {
        return { showChildren : true };
    },
    handleClick: function(course,event) {
        this.setState({showChildren:!this.state.showChildren});
        if(this.state.showChildren && course["isActive"]) {
            ReactDOM.render(<CourseContent id={course["_id"]} type="course" />,
                            document.getElementById('courseContent'));
            event.target.nextElementSibling.className="done";
            ReactDOM.render(<Assignments course={course} />,event.target.nextElementSibling);
        } else {
            event.target.nextElementSibling.className="invisible";
        }
    },
    render: function (){
        var course = this.props.course;
        var classname = "course menuItem navigationText";
        if(course["isActive"]) {
            classname += " active";
        }
        return <li onClick={this.handleClick.bind(this,course)} className={classname}>Course {course["index"]}</li>
    }
});
var MenuNav = React.createClass({
    render: function() {
        var courses = JSON.parse(this.props.courses);
        var courseElems = [];
        for(var i = 0; i<courses.length; i++) {
            courseElems.push(<Course course={courses[i]} />);
            courseElems.push(<ul className="invisible"></ul>);            
        }
        return  <ul>{courseElems}</ul>;
    }
});
getJson("test/test.json",function(courses) {
    ReactDOM.render(<MenuNav courses={courses}/>,document.getElementById("desktopNavigation"));
});
