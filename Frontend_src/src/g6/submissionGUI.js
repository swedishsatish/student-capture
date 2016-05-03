/**
 * Created by c14gls on 2016-05-02.
 */


/**
 * Things that need do be implemented:
 * TODO: Create a function that receives student data such as studentID and course.
 * TODO: Send this information forward to the database, potentially through the submit button.
 * TODO: Also implement a popup window that lets the teacher confirm the grade for the given student.
 */

var hasEdited = false;
/**
 * Creates the comment box used by the GUI.
 * The comment box is used by the teacher to submit feedback to the student.
 */
var CommentBox = React.createClass({

    getInitialState: function () {
        return {currentValue: this.props.children, value: 'Write comments regarding feedback here...'};
    },
    handleChange: function(event) {
        this.setState({value: event.target.value});
    },
    // Upon clicking inside the textField this function sets the text inside to empty.
    onClick: function (event) {
        if(!hasEdited){
            this.setState({value: ''});
        }
        hasEdited=true;
    },
    // The render function for the component CommentBox
    render: function () {
        return (
            <textarea value={this.state.value} onChange={this.handleChange} onClick={this.onClick} />

        );
    }

});
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
        if(isPassed===1){
            passedStatus = 'Pass';
        }else {
            passedStatus = 'No pass'
        }

        //TODO: Create a popup window that lets the teacher confirm the given grading.

        var answer = confirm("You are about to give Kalle a " + passedStatus + " with the following grade: "+dropDownGrade);
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

/**
 * Creates the back button used by the GUI.
 * Makes it possible for the teacher to go back to the previous page.
 */
//Creates the BackButton
var BackButton = React.createClass({
    // Upon clicking on the button, it should go back to the previous GUI window.
    onClick: function() {

        /**
         * TODO: Write code that takes the user back to the previous page.
         */
    },
    // Render function for BackButton
    render: function() {

        return(
            <button id="backbutton" onClick={this.onClick}>Back</button>

        );
    }
});
/**
 * Checkbox that is used to determine if a student passed an assignment or not.
 * @type {number} current state of passed
 */
var isPassed = 0;
var CheckBox = React.createClass({

    //Sets initial state for the checkbox to false.
    getInitialState: function() {
        return {
            checkBox: this.props.checkBox || false

        };
    },
    // Render function for CheckBox
    render: function() {

        return (
            <div id="checklabel">
                <label for="checklabel">
                    <input id="checkbox" type="checkbox"
                           checkBox={this.state.checkBox}
                           onClick={this.handleClick}/>
                    Passed!
                </label>
            </div>
        );
    },
    //Changes from checked to unchecked and vice-versa.
    handleClick: function(e) {

        if(isPassed == 0) {
            isPassed = 1;
        } else {

            isPassed = 0;
        }

        this.setState({
            checkBox: e.target.checkBox
        });

    }
});

/**
 * Drop down menu that lets the teacher choose the grade for the student.
 * @type {string} Sting with students selected grade
 */
// variable to keep track of grade, used in submission button
var dropDownGrade = 'U';
var DropDown = React.createClass({

    getInitialState: function() {
        return {
            value: 'U'
        }
    },

    change: function(event) {
        dropDownGrade = event.target.value;
        this.setState({value: event.target.value})
    },


    render: function() {

        return(
            <div id="dropdown">
                <label for="dropDownMenu">Grade</label>
                <select id="dropDownMenu" onChange={this.change} value={this.state.value}>
                    <option value="U">U</option>
                    <option value="O">O</option>
                    <option value="K">K</option>
                    <option value="G">G</option>
                </select>
            </div>
        )
    }


});

/**
 * Renders all the different built components for the GUI in prioritizing order.
 * For example: First it renders Recorder, then CommentBox and so on.
 */
var RenderHandle = React.createClass({
    render: function () {
        return(
            <div class="row">
                <div id="recorder">
                    <Recorder />
                </div>
                <div id="feedBackContainer">
                    <div id="submissioncontainer">
                        <div id="commentbox">
                            <CommentBox />
                        </div>
                        <div id="gradeAndPassContainer">
                            <div id="passbox">
                                <CheckBox />
                            </div>
                            <div id="dropDown">
                                <DropDown />
                            </div>
                        </div>
                    </div>
                    <div id="buttonContainer">
                        <div id="backButton">
                            <BackButton />
                        </div>
                        <div id="submitButton">
                            <SubmitButton />
                        </div>

                    </div>
                </div>
            </div>
        )
    }

});

window.RenderHandle = RenderHandle;


