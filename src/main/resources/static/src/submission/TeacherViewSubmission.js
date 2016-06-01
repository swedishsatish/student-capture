/**
 *
 *@author: Andreas Savva <ens15asa>,
 *         Benjamin Björklund <c13bbd>,
 *         Tobias Estefors <dv13tes>
 *
 *@note: This react-class is rendered from DynamicMenu.js
 **/

var TeacherViewSubmission = React.createClass({
    nSubmissions: 0, //Antal som svarat
    nWithdrawals: 0, //Antal som lämnat blankt
    nDone: 0, //Totala antalet som gjort uppgiften
    nParticipants: 0, //Totala antalet i kurslistan

        submissionsArray: null, //Alla som har submittat
    participantsArray: null, //Hela kurslistan


    componentWillMount: function () {

        // GET request to database to get all the submissions from the students.
        $.ajax({
            url: "assignments/" + this.props.assignmentId + "/submissions/",
            type: "GET", // Type of http
            async: false,
            success: function (data, status) { // Function to perform when ok
                this.submissionsArray = data;
            }.bind(this),
            error: function (xhr, status, err) {
                // Handle the error
                console.log(xhr + " "+ status+" " + err);
                console.log("TeacherViewSubmission: Error Submissions");
            }.bind(this)
        });
        // GET request to database to get all the participants in a course.
        $.ajax({
            url: "courses/" + this.props.courseId + "/participants", // URL to send to
            type: "GET", // Type of http
            async: false,
            data: {
                "userRole": "student"
            },
            success: function (data, status) { // Function to perform when ok
                this.participantsArray = data;
            }.bind(this),
            error: function (xhr, status, err) {
                // Handle the error
                console.log("TeacherViewSubmission: Error Participants");
            }.bind(this)
        });

    },

    calculateSubmissions: function () {
        this.nSubmissions = 0;
        this.nWithdrawals = 0;
        this.nDone = 0;
        this.nParticipants = 0;
        this.nParticipants = this.participantsArray.length;
        for (var i=0;i<this.submissionsArray.length;i++) {
            if(this.submissionsArray[i].status) {
                if (this.submissionsArray[i].status.toLowerCase() == "answer") {
                    this.nSubmissions++;
                } else if (this.submissionsArray[i].status.toLowerCase() == "blank") {
                    this.nWithdrawals++;
                }
            }
        }

        this.nDone = this.nSubmissions + this.nWithdrawals;
    },


    render: function () {
        var idArray=[{courseID:this.props.courseId,
                     assignmentID:this.props.assignmentId}];
        this.calculateSubmissions();
        return (
            <div class="row">
                <div className="four columns offset-by-one">

                    <div id="studentContainer">
                        <StudentList submissions={this.submissionsArray} idArray={idArray} participants={this.participantsArray} scale={this.props.scale}/>
                        <div id="textList">
                            <p>Submissions: {this.nSubmissions}/{this.nParticipants}</p>
                            <p>Withdrawals: {this.nWithdrawals}</p>
                            <p>Done: {this.nDone}/{this.nParticipants}</p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
});

window.TeacherViewSubmission = TeacherViewSubmission;