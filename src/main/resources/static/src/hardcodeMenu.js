var CourseList = React.createClass({
    printVideo : function() {
        ReactDOM.render(<AssignmentContent course="kurs 3" assignment="assignment 2"/>,document.getElementById("courseContent"));
    },
    
    studentSubmissions: function () {
        ReactDOM.render(<TeacherViewSubmission />, document.getElementById('courseContent'));
    },

    printFeedback : function() {
        ReactDOM.render(<Feedback />,document.getElementById("courseContent"));
    },

    newAssignment : function() {
        ReactDOM.render(<NewAssignment />, document.getElementById('courseContent'));
    },
    teacherRecordVideo : function() {
        ReactDOM.render(<TeacherRecordVideo />, document.getElementById('courseContent'));
    },
    studentRecordVideo: function () {
        ReactDOM.render(<StudentRecordVideo />, document.getElementById('courseContent'));
    },
    settings: function () {
        ReactDOM.render(<Settings userID="13"/>, document.getElementById('courseContent'));
    },
    render: function () {
        var listComponents = [];
        listComponents.push(<div className="menuItem" key={1} onClick={this.printVideo}>
            <div><h6 className="navigationText">Video Playback</h6></div>
        </div>);
        listComponents.push(<div className="menuItem" key={2} onClick={this.printFeedback}>
            <div><h6 className="navigationText">Feedback</h6></div>
        </div>);
        listComponents.push(<div className="menuItem" key={3} onClick={this.studentSubmissions}>
            <div><h6 className="navigationText">Teacher view assignment</h6></div>
        </div>);
        listComponents.push(<div className="menuItem" key={4} onClick={this.newAssignment}>
            <div><h6 className="navigationText">Teacher new assignment</h6></div>
        </div>);
        listComponents.push(<div className="menuItem" key={5} onClick={this.teacherRecordVideo}>
            <div><h6 className="navigationText">Teacher Record Video</h6></div>
        </div>);
        listComponents.push(<div className="menuItem" key={6} onClick={this.studentRecordVideo}>
            <div><h6 className="navigationText">Student Record Video</h6></div>
        </div>);
        listComponents.push(<div className="menuItem" key={7} onClick={this.settings}>
            <div><h6 className="navigationText">Settings</h6></div>
        </div>);
        return <div>{listComponents}</div>;
    }
});
ReactDOM.render(<CourseList />, document.getElementById('desktopNavigation')); 
