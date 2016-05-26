/**
 * Submit button for teacher feedback, will ask for
 * confirmation from teacher then send data to database.
 * @author: dv13trm, c14gls, group 6
 */

/**
 * Cancel button for popup window, closes popup.
 */




var student;
var IDs;
var video;
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
        //sendVideo(this.props.getVideo);
       // sendFeedBackVideo();
        sendData();
        close();
        ReactDOM.render(<TeacherViewSubmission courseId={IDs[0].courseID} assignmentId={IDs[0].assignmentID}/>,document.getElementById('courseContent'));

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
            <p id="smallLetter">{student[0].studentName}</p>
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
                <PopUpStudentName/> <p id="smallLetter">a</p>
                <PopUpPassBox/> <p id="smallLetter">with grade</p> <PopUpGrade/>
                <br/>
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
function submitForm(method) {

    var reqBody = {};
    reqBody["feedback"] = document.getElementById('teachercomments').value;
    reqBody["grade"] = {};
    reqBody["grade"]["grade"] = document.getElementById('dropDownMenu').value;
    reqBody["grade"]["teacherID"] = "7777777"; //TODO: Fix this grade: document.getElementById('dropDownMenu').value;
    reqBody["studentPass"] = document.getElementById('ifStudentPass').checked;
    reqBody["publishStudentSubmission"] = document.getElementById('PermissionFromStudent').checked;
    reqBody["courseID"] = IDs[0].courseID;

    $.ajax({
        type: method,
        contentType: "application/json",
        url: "assignments/" + IDs[0].assignmentID + "/submissions/" + student[0].studentID,//98 byts til student[0].studentID senare
        data : JSON.stringify(reqBody),
        timeout: 100000,
        success: function (response) {
            console.log("SUCCESS: ", response);
            console.log("SUCCESS reqBody contains:", reqBody);
            // TODO: check response with if/else, if respons is fail give error message

          //  ReactDOM.render(<div>HEJ</div>, document.getElementById('courseContent'));
        }, error: function (e) {
            console.log("ERROR: ", e);
            console.log("ReqBody contains:", reqBody);
        }, done: function (e) {
            console.log("DONE");
        }
    });

}
/*
 * Function that is responsible for sending the teachers feedback video to the student.
 * Parameters: method, which method that is to be used (POST, PUT , PATCH)
 */

function  getForm(method) {


   /* var reqBody = {};
    reqBody["feedback"] = document.getElementById('teachercomments').value;
    reqBody["grade"] = {};
    reqBody["grade"]["grade"] = document.getElementById('dropDownMenu').value;
    reqBody["grade"]["teacherID"] = "7777777"; //TODO: Fix this grade: document.getElementById('dropDownMenu').value;
    reqBody["studentPass"] = document.getElementById('ifStudentPass').checked;
    reqBody["publishStudentSubmission"] = document.getElementById('PermissionFromStudent').checked;
    reqBody["courseID"] = IDs[0].courseID; */
    console.log("start");
    console.log("assignments/" + IDs[0].assignmentID + "/submissions/" + student[0].studentID);


    $.ajax({
        url: "assignments/" + IDs[0].assignmentID + "/submissions/" + student[0].studentID,
        timeout: 100000,
        success: function (response) {
            console.log("HEJ Sucsess");
            console.log(response);
            console.log("HEJ ");
            //console.log(response);
            //document.setElementById("Course") = response["course"] == null ? "" : response["course"];
            document.getElementById('teachercomments').value = response["status"] == null ? "" : response["feedback"];
            console.log("HEJ2");
            document.getElementById('dropDownMenu').value = response["grade"]["grade"] == null ? "U" : response["grade"]["grade"];
            //document.getElementById('dropDownMenu').value = responseData["grade"]["grade"];
            console.log("HEJ4");
            //document.getElementById('ifStudentPass').checked = response["studentPass"] == null "false" : response["studentPass"];
            //document.getElementById('ifStudentPass').checked = responseData["studentPass"];
            //console.log("response[publishStudentSubmission] :"+response["publishStudentSubmission"]);
            console.log("response[studentPublishConsent] :"+response["studentPublishConsent"]);
            console.log("1CheckIf:"+response["studentPublishConsent"] ? "false" : "true");
            console.log("2CheckIf:"+response["studentPublishConsent"] ? "1" : "2");

            document.getElementById('PermissionFromStudent').disabled = response["studentPublishConsent"] ? false : true;
            document.getElementById('PermissionFromStudent').checked = response["publishStudentSubmission"] ? true : false;
            //document.getElementById('PermissionFromStudent').checked = ["publishStudentSubmission"];

        }, error: function (e) {
            console.log("FAIL HUE");
            console.log("ERROR: ", e);
            console.log("ReqBody contains:", reqBody);
        }, done: function (e) {
            console.log("DONE");
        }
    });

}
function sendFeedBackVideo() {

        console.log(IDs);
        console.log(student);

        var request = {};
      //  request["feedbackVideo"] = recordVideo.getBlob;//kod för att hämta videon.
      //  request["courseID"] = IDs[0].courseID;

        var fd = new FormData();

        fd.append("feedbackVideo", recordVideo.getBlob);

        fd.append('courseID', new Blob([JSON.stringify({
            courseID: IDs[0].courseID
        })], {
            type: "application/json"
        }));


        $.ajax({
            type: "POST",
            url: "assignments/" + IDs[0].assignmentID + "/submissions/" + student[0].studentID + "/feedbackvideo/",
            data : fd,
            contentType: false,
            processData: false,
            timeout: 100000,
            success: function (response) {

                console.log("VIDEO SUCCESS: ", response);
                console.log("SUCCESS fd contains:", request);

                // TODO: check response with if/else, if respons is fail give error message

            }, error: function (e) {

                console.log("VIDEO ERROR: ", e);
                console.log("ERROR fd contains:", request);

            }, done: function (e) {
                console.log("DONE");
            }
        });

}
/**
 * Sending data to database.
 */
 /* Sends all data except the video */
function sendData () {

    submitForm('PATCH');
}

/* Sends the recorded video */
function sendVideo(getVideoFunc) {

    submitVideo('POST', getVideoFunc);

}


/**
 * Creates the submit button used by the GUI.
 * Upon clicking on the button it sends the feedback information to the student.
 */
var SubmitButton = React.createClass({


    componentWillMount: function(){
        student=this.props.studentArray;
        IDs=this.props.idArray;
        video = this.props.getVideo;
        if(student[0].studentID){
            console.log("före get");
            getForm('GET'); // retunerar null??? så kanske inte nått i databas??
            console.log("efter get");
        }
    },
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
        ReactDOM.render(<PopUpRender getVideo={this.props.getVideo} student={this.props.student}/>,document.getElementById('popUpDiv'));
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