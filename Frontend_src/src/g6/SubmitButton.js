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

    onClick: function() {
        // answer contains true if Ok is pressed., false if cancel is pressed.
        var passedStatus = 'No pass';
        if(document.getElementById('ifStudentPass').checked){
            passedStatus = 'Pass';
        }else {
            passedStatus = 'No pass'
        }

        //TODO: Create a popup window that lets the teacher confirm the given grading.

        var answer = confirm("You are about to give Kalle a " + passedStatus + " with the following grade: "+document.getElementById('dropDownMenu').value);
        if(answer){

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