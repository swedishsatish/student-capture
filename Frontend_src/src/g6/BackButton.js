/**
 * Back button for teacher feedback
 * @author: dv13trm, c14gls, group 6
 */


/**
 * Creates the back button used by the GUI.
 * Makes it possible for the teacher to go back to the previous page.
 */
//Creates the BackButton
var BackButton = React.createClass({
    // Upon clicking on the button, it should go back to the previous GUI window.
    /**
     * Rerender the studentList
     */
    onClick: function() {

        ReactDOM.render(<StudentList/>,document.getElementById('courseContent'));

    },
    // Render function for BackButton
    render: function() {

        return(
            <button id="backbutton" onClick={this.onClick}>Back</button>

        );
    }
});

window.BackButton = BackButton;