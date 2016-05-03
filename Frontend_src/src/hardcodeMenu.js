var CourseList = React.createClass({
    printVideo : function() {
        ReactDOM.render(<AssignmentContent course="kurs 3" assignment="assignment 2"/>,document.getElementById("courseContent"));
    },
	render : function() {
		var listComponents = [];
        listComponents.push(<div className="menuItem" key={1} onClick={this.printVideo} ><h6 className="navigationText">Item 1</h6></div>);
        listComponents.push(<div className="menuItem" key={2}><h6 className="navigationText">Item 2</h6></div>);
        listComponents.push(<div className="menuItem" key={3}><h6 className="navigationText">Item 3</h6></div>);
        listComponents.push(<div className="menuItem" key={4}><h6 className="navigationText">Item 4</h6></div>);
		return <div>{listComponents}</div>;
	}
});
ReactDOM.render(<CourseList />, document.getElementById('desktopNavigation')); 
