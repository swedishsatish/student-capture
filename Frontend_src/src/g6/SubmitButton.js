/**
 * Created by sejiaw on 2016-05-10.
 */


var PopUpCloseButton = React.createClass({
    onclick: function () {
        close();
    },
    render: function () {
        return(
            <button id="cancelButton" onClick={this.onclick}>Cancel</button>
        )
    }

});
var PopUpOkButton = React.createClass({
    onclick: function () {
        sendData();
        close();
    },
    render: function () {
        return(
            <button id="okButton" onClick={this.onclick}>ok </button>
        )
    }
});
var PopUpPassBox = React.createClass({
    render: function () {
        if(document.getElementById('ifStudentPass').checked){
            return(
                <h2 id="popUpPass">PASS</h2>
            )
        }else{
            return(
                <h2 id="popUpFail">NOT PASSED</h2>
            )
        }
    }


});
var PopUpGrade = React.createClass({

    render: function () {
        return(
            <h2 id="U">{document.getElementById('dropDownMenu').value}</h2>
        )


    }
});
var PopUpStudentName = React.createClass({
    render: function () {
        var studentname = 'error';
        if(studentname != 'error'){
            //studentname = user.studentname??
            //return studentname
        }else{
            return(
                <h2 id="uniquestudent">Error Errorsson</h2>
            )
        }
    }
});


var PopUpRender = React.createClass({
    render: function () {
        return(
            <div class="row">
                <PopUpCloseButton/>
                <PopUpOkButton/>
                <PopUpPassBox/>
                <PopUpGrade/>
                <PopUpStudentName/>
            </div>
        )
    }
});
function close(){
    var popUpElement = document.getElementById('popUpDiv');
    var blanketElement = document.getElementById('blanket');
    blanketElement.style.display='none';
    popUpElement.style.display='none';
}
function submitForm() {
    var reqBody = {};
    reqBody["TeacherComments"] = document.getElementById('teachercomments').value;
    reqBody["DropDown"] = document.getElementById('dropDownMenu').value;
    reqBody["StudentPass"] = document.getElementById('ifStudentPass').checked;
    reqBody["AssignmentID"] = 1;
    reqBody["CourseID"] = 2;
    reqBody["TeacherID"] = 3;
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "feedback",
        data: JSON.stringify(reqBody),
        timeout: 100000,
        success: function (response) {
            console.log("SUCCESS: ", response);
            ReactDOM.render(<div>HEJ</div>, document.getElementById('courseContent'));
        }, error: function (e) {
            console.log("ERROR: ", e);
        }, done: function (e) {
            console.log("DONE");
        }
    });
}
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
 * See TODO: on top of page.
 * Upon clicking on the button it sends the feedback information to the student.(Not yet implemented)
 */


var SubmitButton = React.createClass({
    // Upon clicking on the button a popup confirming window is shown,

    toggle: function(div_id) {
        var element = document.getElementById(div_id);
        if ( element.style.display == 'none' ) {
            element.style.display = 'block';
        }
        else {
            element.style.display = 'none';
        }
    },popUpConfirmation: function (div_id) {
        this.blanket_size(div_id);
        this.window_pos(div_id);
        this.toggle("blanket");
        this.toggle(div_id);
        this.popUpRender();


    },
    popUpRender: function () {
        ReactDOM.render(<PopUpRender/>,document.getElementById('popUpDiv'));
    }, blanket_size: function(popUpDivVar) {
        var popUpDiv_height;
        var viewportheight;
        var blanket_height;

        if (typeof window.innerWidth != 'undefined') {
            viewportheight = window.innerHeight;
        } else {
            viewportheight = document.documentElement.clientHeight;
        }
        if ((viewportheight > document.body.parentNode.scrollHeight) && (viewportheight > document.body.parentNode.clientHeight)) {
            blanket_height = viewportheight;
        } else {
            if (document.body.parentNode.clientHeight > document.body.parentNode.scrollHeight) {
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
    },window_pos: function(popUpDivVar) {
        var viewportwidth;
        var window_width;
        if (typeof window.innerWidth != 'undefined') {
            viewportwidth = window.innerHeight;
        } else {
            viewportwidth = document.documentElement.clientHeight;
        }
        if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)) {
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
     *     <div id="blanket" style="display:none;"></div>
     <div id="popUpDiv" style="display:none;">
     <button id="okButton">Ok</button>
     <button id="closeButton">Cancel</button>
     </div>
     * TODO: Send information to the database.
     * TODO: Information is defined as grade, feedback comments and so on.
     */
    // Render function for SubmitButton
    render: function () {
        return (
            <button id="submitbutton" onClick={this.onClick}>Submit</button>
        );
    }
});
window.SubmitButton=SubmitButton;