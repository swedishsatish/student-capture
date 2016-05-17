/**
 *
 *@author: Tobias Estefors <dv13tes>
 **/

var TeacherViewSubmission = React.createClass({
    nSubmissions: 0, //Antal som svarat
    nWithdrawals: 0, //Antal som lämnat blankt
    nDone: 0, //Totala antalet som gjort uppgiften
    nParticipants: 0, //Totala antalet i kurslistan
    
    submissionsArray: null,
    participantsArray: null,

    componentWillMount: function () {
        this.nSubmissions = 0;
        this.nWithdrawals = 0;
        this.nDone = 0;
        this.nParticipants = 0;

        // GET request to database to get all the submissions from the students.
        //SKA AVKOMMENTERAS SENARE
      /**  $.ajax({
            url: window.globalURL + "/DB/getAllSubmissions", // URL to send to
            type: "GET", // Type of http
            async: false,
            data: {assignmentID: 1200},
            success: function (data, status) { // Function to perform when ok
                this.submissionsArray = data;
            }.bind(this),
            error: function (xhr, status, err) {
                // Handle the error
                console.log("Error Submissions");
            }.bind(this)
        });

        // GET request to database to get all the participants in a course.
        $.ajax({
            url: window.globalURL + "/DB/getAllParticipantsFromCourse", // URL to send to
            type: "GET", // Type of http
            async: false,
            data: {courseID: 1200},
            success: function (data, status) { // Function to perform when ok
                this.participantsArray = data;
            }.bind(this),
            error: function (xhr, status, err) {
                // Handle the error
                console.log("Error Participants");
            }.bind(this)
        });**/
    },

    /* kommenterar ska tas bort
    calculateSubmissions: function () {
        this.nParticipants = this.participantsArray.length;
        
        for (var i=0;i<this.submissionsArray.length;i++) {
            if (this.submissionsArray[i].status == "Answer") {
                this.nSubmissions++;
            } else if (this.submissionsArray[i].status == "Blank") {
                this.nWithdrawals++;
            }
        }

        this.nDone = this.nSubmissions + this.nWithdrawals;
    },*/


    //DENNA FUNKTION SKA TAS BORT
    calculateSubmissions: function () {
        var userList=window.users;
        for(var i=0; i < userList.length;i++){
            console.log(userList[i].studentName.value)
            if(userList[i].withdraw){
                this.nWithdrawals++;
                continue;
            }
            this.nSubmissions++;
        }
        this.nDone=this.nSubmissions+this.nWithdrawals;
        this.nParticipants=userList.length;
    },

    render: function () {
        this.calculateSubmissions();
        
        return (
            <div class="row">
                <div className="four columns offset-by-one">

                    <div id="studentContainer">
                        <StudentList submissions={this.submissionsArray} participants={this.participantsArray} />
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