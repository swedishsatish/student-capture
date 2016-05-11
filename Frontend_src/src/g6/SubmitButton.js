/**
 * Created by sejiaw on 2016-05-10.
 */
/**
 * Created by sejiaw on 2016-05-10.
 */


/**
 * Creates the submit button used by the GUI.
 * See TODO: on top of page.
 * Upon clicking on the button it sends the feedback information to the student.(Not yet implemented)
 */

var SubmitButton = React.createClass({
    // Upon clicking on the button a popup confirming window is shown,
    submitForm: function(){
        var reqBody = {};
        reqBody["TeacherComments"] = document.getElementById('teachercomments').value;
        reqBody["DropDown"] = document.getElementById('dropDownMenu').value;
        reqBody["StudentPass"] = document.getElementById('ifStudentPass').checked;
        reqBody["AssignmentID"] = 1;
        reqBody["CourseID"] = 2;
        reqBody["TeacherID"] =3;
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "feedback",
            data : JSON.stringify(reqBody),
            timeout : 100000,
            success : function(response) {
                console.log("SUCCESS: ", response);
                ReactDOM.render(<div>HEJ</div>, document.getElementById('courseContent'));
            }, error : function(e) {
                console.log("ERROR: ", e);
            }, done : function(e) {
                console.log("DONE");
            }
        });
    },

    onClick: function() {
        // answer contains true if Ok is pressed., false if cancel is pressed.
        var passedStatus = 'No pass';
        // alert(document.getElementById('teachercomments').value);
        if(document.getElementById('ifStudentPass').checked){
            passedStatus = 'Pass';
        }else {
            passedStatus = 'No pass'
        }
        //TODO: Create a popup window that lets the teacher confirm the given grading.

        var answer = confirm("You are about to give Kalle a " + passedStatus + " with the following grade: "+document.getElementById('dropDownMenu').value);
        if(answer){
            this.submitForm();
        }else{
            //Go back to grading page?
            void(0);
        }
    },
    /**
     * TODO: Send information to the database.
     * TODO: Information is defined as grade, feedback comments and so on.
     */
    // Render function for SubmitButton
    render: function () {
        return (
            <button id="submitbutton" onClick={this.onClick}>Submit
            </button>


        );
    }
});
window.SubmitButton=SubmitButton;