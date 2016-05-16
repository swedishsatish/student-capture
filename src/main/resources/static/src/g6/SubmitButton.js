/**
 * Created by sejiaw on 2016-05-10.
 */

/**
 * Cancel button for popup window, closes popup.
 */
var PopUpCancelButton = React.createClass({
    onclick: function () {
        close();
    },
    render: function () {
        return(
            <button id="cancelButton" onClick={this.onclick}>Cancel</button>
        )
    }

});
/**
 * Confirmation button for popup, will send data to database then close window.
 */
var PopUpConfirmButton = React.createClass({
    onclick: function () {
        sendData();
        close();
        ReactDOM.render(<StudentList/>,document.getElementById('courseContent'));

    },
    render: function () {
        return(
            <button id="confirmationButton" onClick={this.onclick}>Save</button>
            
        )
    }
});
/**
 * Pass text for popup window, will get status from
 * checkbox then either put PASS or NOT PASSED in text.
 */
var PopUpPassBox = React.createClass({
    render: function () {
        if(document.getElementById('ifStudentPass').checked){
            return(
                <p id="popUpPass">PASS</p>
            )
        }else{
            return(
                <p id="popUpFail">NOT PASSED</p>
            )
        }
    }


});
/**
 * Grade for popup window, will get grade teacher selected
 * from previous window and put that into confirmation text.
 */
var PopUpGrade = React.createClass({

    render: function () {
        return(
            <p id="popUpGrade">{document.getElementById('dropDownMenu').value}</p>
        )


    }
});
/**
 * Student name for popup window, will get student
 * selected for grading then put it into confirmation text.
 */
var PopUpStudentName = React.createClass({
    render: function () {

        return(
            <p id="smallLetter">{this.props.student}</p>
        )

    }
});
/**
 * Render text for popup window, this object do only create text for window.
 */
var PopUpRender = React.createClass({
    render: function () {
        return(
            <div class="row">
                <p id="smallLetter">You are about to give</p>
                <PopUpStudentName student={this.props.student}/> <p id="smallLetter">a</p>
                <PopUpPassBox/> <p id="smallLetter">with grade</p> <PopUpGrade/>
                <PopUpStudentName student={this.props.student}/>
                <p id="smallLetter">will be notified</p>
                <div id="popUpButtonContainer">
                    <PopUpCancelButton/>
                    <PopUpConfirmButton/>

                </div>

            </div>
        )
    }
});
/**
 * Hides popup window.
 */
function close(){
    var popUpElement = document.getElementById('popUpDiv');
    var blanketElement = document.getElementById('blanket');
    blanketElement.style.display='none';
    popUpElement.style.display='none';
}
/**
 * Submits data that will sent later, data in reqBody will be sent.
 */
function submitForm() {
    var reqBody = {};
    reqBody["TeacherComments"] = document.getElementById('teachercomments').value;
    reqBody["Grade"] = document.getElementById('dropDownMenu').value;
    reqBody["StudentPass"] = document.getElementById('ifStudentPass').checked;
    reqBody["ShareData"] = document.getElementById('PermissionFromStudent').checked;
    reqBody["AssignmentID"] = window.assignmentID;
    reqBody["CourseID"] = window.courseID;
    reqBody["TeacherID"] = window.teacherID;
    reqBody["value"] = "set";


    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "feedback",
        data: JSON.stringify(reqBody),
        timeout: 100000,
        success: function (response) {
            console.log("SUCCESS: ", response);
            // TODO: check response with if/else, if respons is fail give error message

          //  ReactDOM.render(<div>HEJ</div>, document.getElementById('courseContent'));
        }, error: function (e) {
            console.log("ERROR: ", e);
        }, done: function (e) {
            console.log("DONE");
        }
    });
}
/**
 * Sending data to database.
 */
function sendData () {
    // answer contains true if Ok is pressed., false if cancel is pressed.
    var passedStatus = 'No pass';
    // alert(document.getElementById('teachercomments').value);
    if (document.getElementById('ifStudentPass').checked) {
        passedStatus = 'Pass';
    } else {
        passedStatus = 'No pass'
    }
    submitForm();

}


/**
 * Creates the submit button used by the GUI.
 * Upon clicking on the button it sends the feedback information to the student.
 */
var SubmitButton = React.createClass({
    /**
     * Used to toggle on divs, make them visable.
     * @param div_id div that will be toggled.
     */
    toggle: function(div_id) {
        var element = document.getElementById(div_id);
        if ( element.style.display == 'none' ) {
            element.style.display = 'block';
        }
        else {
            element.style.display = 'none';
        }
    },
    /**
     * Crates popup on selected div.
     * @param div_id div that will generate popup.
     */
    popUpConfirmation: function (div_id) {
        this.blanket_size(div_id);
        this.window_pos(div_id);
        this.toggle("blanket");
        this.toggle(div_id);
        this.popUpRender();


    },
    /**
     * Rendering popup window in react class.
     */
    popUpRender: function () {
        ReactDOM.render(<PopUpRender student={this.props.student}/>,document.getElementById('popUpDiv'));
    },
    /**
     * Set size for popup blanket.
     * @param popUpDivVar Div that blanket will be generated on.
     */
    blanket_size: function(popUpDivVar) {
        var popUpDiv_height;
        var viewportheight;
        var blanket_height;
        if (typeof window.innerWidth != 'undefined') {
            viewportheight = window.innerHeight;
        } else {
            viewportheight = document.documentElement.clientHeight;
        }
        if ((viewportheight > document.body.parentNode.scrollHeight) &&
            (viewportheight > document.body.parentNode.clientHeight)) {
            blanket_height = viewportheight;
        } else {
            if (document.body.parentNode.clientHeight >
                document.body.parentNode.scrollHeight) {
                blanket_height = document.body.parentNode.clientHeight;
            } else {
                blanket_height = document.body.parentNode.scrollHeight;
            }
        }
        var blanket = document.getElementById('blanket');
        blanket.style.height = blanket_height + 'px';
        var popUpDiv = document.getElementById(popUpDivVar);
        popUpDiv_height=blanket_height/2-200;//200 is half popup's height
        popUpDiv.style.top = popUpDiv_height + 'px';
    },
    /**
     * Calculates window position based on window size.
     * @param popUpDivVar div that will be used for window.
     */
    window_pos: function(popUpDivVar) {
        var viewportwidth;
        var window_width;
        if (typeof window.innerWidth != 'undefined') {
            viewportwidth = window.innerHeight;
        } else {
            viewportwidth = document.documentElement.clientHeight;
        }
        if ((viewportwidth > document.body.parentNode.scrollWidth) &&
            (viewportwidth > document.body.parentNode.clientWidth)) {
            window_width = viewportwidth;
        } else {
            if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
                window_width = document.body.parentNode.clientWidth;
            } else {
                window_width = document.body.parentNode.scrollWidth;
            }
        }
        var popUpDiv = document.getElementById(popUpDivVar);
        window_width=window_width/2-200;//200 is half popup's width
        popUpDiv.style.left = window_width + 'px';
    },
    /**
     * onclick function for submit button.
     * Will check if textarea is empty then ask for confirmation
     * from user if they want to leave it empty. If yes popup window will be generated.
     */
    onClick: function() {
        if(document.getElementById('teachercomments').value===''){
            var saftyCheck = confirm("Are you sure you want to leave comment box empty?");
            if(saftyCheck){
                this.popUpConfirmation('popUpDiv');
            }else{
                void(0);
            }
        }else{
            this.popUpConfirmation('popUpDiv');
        }
    },
    /**
     * Render function for submitbutton.
     * @returns {XML} A button.
     */
    render: function () {
        return (
            <button id="submitbutton" onClick={this.onClick}>Submit</button>
        );
    }
});
window.SubmitButton=SubmitButton;