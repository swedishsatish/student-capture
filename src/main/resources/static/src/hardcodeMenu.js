var CourseList = React.createClass({
    printVideo : function() {
        ReactDOM.render(<AssignmentContent course="kurs 3" assignment="assignment 2"/>,document.getElementById("courseContent"));
    },
    
    studentSubmitions: function () {
         $.get(window.globalURL + "/DB/getAllSubmissions",{assignmentID:1000},function (res) {
             //var users = JSON.parse(res);
             console.log(res);
             //console.log(users);
             ReactDOM.render(<StudentList students={res}/>, document.getElementById('courseContent'));
         });
        var users = [{
            videoURL:"http://www.w3schools.com/html/mov_bbb.mp4",
            assignmentID: 1000,
            studentID: 21,
            studentName: "Anton Andersson",
            submissionDate: "2010-06-23",
            withdraw: false,
            grade: "G",
            teacherID: 12
        },
            {
                videoURL:"http://techslides.com/demos/sample-videos/small.mp4",
                assignmentID: 1000,
                studentID: 21,
                studentName: "Lukas Lundberg",
                submissionDate: "2010-06-23",
                grade: "K",
                teacherID: 12,
                withdraw: false
            },
            {
                videoURL:"http://easyhtml5video.com/assets/video/new/Penguins_of_Madagascar.mp4",
                assignmentID:213,
                studentID:2,
                studentName: "Daniel Eliasson",
                submissionDate: "2012-05-23",
                grade: "U",
                teacherID: 221,
                withdraw:true
            }
        ];
        window.users=users;
        console.log("jebjeb")
        ReactDOM.render(<TeacherViewSubmission students={users}/>, document.getElementById('courseContent'));
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
    studentRecordVideo : function() {
            ReactDOM.render(<StudentRecordVideo />, document.getElementById('courseContent'));
        },
	render : function() {
		var listComponents = [];
        listComponents.push(<div className="menuItem" key={1} onClick={this.printVideo} ><div><h6 className="navigationText">Video Playback</h6></div></div>);
        listComponents.push(<div className="menuItem" key={2} onClick={this.printFeedback}><div><h6 className="navigationText">Feedback</h6></div></div>);
        listComponents.push(<div className="menuItem" key={3} onClick={this.studentSubmitions}><div><h6 className="navigationText">Teacher view assignment</h6></div></div>);
        listComponents.push(<div className="menuItem" key={4} onClick={this.newAssignment}><div><h6 className="navigationText">Teacher new assignment</h6></div></div>);
        listComponents.push(<div className="menuItem" key={5} onClick={this.teacherRecordVideo}><div><h6 className="navigationText">Teacher Record Video</h6></div></div>);
        listComponents.push(<div className="menuItem" key={6} onClick={this.studentRecordVideo}><div><h6 className="navigationText">Student Record Video</h6></div></div>);

		return <div>{listComponents}</div>;
	}
});
ReactDOM.render(<CourseList />, document.getElementById('desktopNavigation')); 
