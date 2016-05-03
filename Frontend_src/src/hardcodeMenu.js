var CourseList = React.createClass({
    printVideo : function() {
        ReactDOM.render(<AssignmentContent course="kurs 3" assignment="assignment 2"/>,document.getElementById("courseContent"));
    },
    printFeedback : function() {
        ReactDOM.render(<Feedback />,document.getElementById("courseContent"));
    },
	render : function() {
		var listComponents = [];
        listComponents.push(<div className="menuItem" key={1} onClick={this.printVideo} ><h6 className="navigationText">Video Playback</h6></div>);
        listComponents.push(<div className="menuItem" key={2} onClick={this.printFeedback}><h6 className="navigationText">Feedback</h6></div>);
        listComponents.push(<div className="menuItem" key={3}><h6 className="navigationText">Item 3</h6></div>);
        listComponents.push(<div className="menuItem" key={4}><h6 className="navigationText">Item 4</h6></div>);
		return <div>{listComponents}</div>;
	}
});
ReactDOM.render(<CourseList />, document.getElementById('desktopNavigation')); 
