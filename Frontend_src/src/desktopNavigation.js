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
	{name:'course 3', id:'4'},
	{name:'course 3', id:'5'},
	{name:'course 3', id:'6'},
	{name:'course 3', id:'7'},
	{name:'course 3', id:'8'},
	{name:'course 3', id:'9'},
	{name:'course 3', id:'10'},
	{name:'course 3', id:'11'},
	{name:'course 3', id:'12'},
	{name:'course 3', id:'13'},
	{name:'course 4', id:'14'},
	{name:'course 3', id:'15'},
	{name:'course 3', id:'16'},
	{name:'course 3', id:'17'},
	{name:'course 3', id:'18'},
	{name:'course 3', id:'19'},
];

ReactDOM.render(<CourseList courses = {courses} />, document.getElementById('desktopNavigation'));