var CourseList = React.createClass({
	render : function() {
		var listComponents = this.props.courses.map(function(course) {
			return <div className="menuItem" key={course.id}><h6 className="navigationText">{course.name}</h6></div>
		});
		return <div>{listComponents}</div>;
	}
});

var courses = [
	{name:'course 1', id:'1'},
	{name:'course 2', id:'2'},
	{name:'course 3', id:'3'},
	{name:'course 4', id:'4'}
];

ReactDOM.render(<CourseList courses = {courses} />, document.getElementById('desktopNavigation'));