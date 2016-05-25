var NewAssignment = React.createClass({
    getInitialState: function() {
        return {courseID : 0, errorMessage : ""};
    },
    update: function () {
        $("#startDate").datetimepicker(
            {
                dateFormat: "yy-mm-dd",
                timeFormat: "HH:mm:ss",
                /*
                 minDate
                 jQuery datepicker option
                 which set today date as minimum date
                 */
                minDate: 0
            });
        $("#endDate").datetimepicker(
            {
                dateFormat: "yy-mm-dd",
                timeFormat: "HH:mm:ss",
                /*
                 minDate
                 jQuery datepicker option
                 which set today date as minimum date
                 */
                minDate: 0
            });
        $("#publish").datetimepicker(
            {
                dateFormat: "yy-mm-dd",
                timeFormat: "HH:mm:ss",
                /*
                 minDate
                 jQuery datepicker option
                 which set today date as minimum date
                 */
                minDate: 0
            });

        tinymce.remove();
        tinymce.init({
            selector: 'textarea.inputField',
            theme: 'modern',
            plugins: [
                'advlist autolink lists link image charmap preview hr anchor pagebreak',
                'searchreplace wordcount visualblocks visualchars code fullscreen',
                'insertdatetime nonbreaking save table contextmenu directionality',
                'template paste textcolor colorpicker textpattern imagetools autoresize'
            ],
            autoresize_max_height: 300,
            toolbar1: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
            toolbar2: 'preview | forecolor backcolor',
            image_advtab: true,
            templates: [
                { title: 'Test template 1', content: 'Test 1' },
                { title: 'Test template 2', content: 'Test 2' }
            ],
            content_css: [
                '//fast.fonts.net/cssapi/e6dc9b99-64fe-4292-ad98-6974f93cd2a2.css',
                '//www.tinymce.com/css/codepen.min.css'
            ]
        });

        if (this.props.edit) {
            this.getAssignmentData();
        }
    },
    componentDidUpdate : function() {
        document.getElementById("newAssError").scrollIntoView();
        this.update();
    },
    componentDidMount : function() {
        this.update();
    },
    getAssignmentData() {
        $.ajax({
            type : "GET",
            contentType : "application/json",
            url : "assignments/" + this.props.assID,
            timeout : 100000,
            success : function(response) {
                document.getElementById("title").value = response.title;
                document.getElementById("minTimeSeconds").value = response.videoIntervall.minTimeSeconds;
                document.getElementById("maxTimeSeconds").value = response.videoIntervall.maxTimeSeconds;
                document.getElementById("startDate").value = response.assignmentIntervall.startDate;
                document.getElementById("endDate").value = response.assignmentIntervall.endDate;
                document.getElementById("publish").value = response.assignmentIntervall.publishedDate;
                tinymce.get('description').setContent(response.description);
                tinymce.get('recap').setContent(response.recap);
                document.getElementById("scale").value = response.scale;

            }.bind(this),
            error : function(e) {
                console.log("ERROR: ", e);
                this.setState({errorMessage : "Could not load assignment. Contact support or try again later"});
            }.bind(this),
            done : function(e) {
                console.log("DONE");
            }
        });
    },
    submitAssignment: function() {
        var reqBody = {};
        var videoIntervall = {};
        var assignmentIntervall = {};

        if (this.props.edit) {
            var type = "PUT";
            reqBody["assignmentID"] = this.props.assID;
        } else {
            var type = "POST";
        }

        videoIntervall["minTimeSeconds"] = $("#minTimeSeconds").val();
        videoIntervall["maxTimeSeconds"] = $("#maxTimeSeconds").val();
        assignmentIntervall["startDate"] = $("#startDate").val();
        assignmentIntervall["endDate"] = $("#endDate").val();
        assignmentIntervall["publishedDate"] = $("#publish").val();
        reqBody["courseID"] = this.props.courseID;
        reqBody["title"] = $("#title").val();
        reqBody["description"] = tinymce.get('description').getContent();
        reqBody["videoIntervall"] = videoIntervall;
        reqBody["assignmentIntervall"] = assignmentIntervall;
        reqBody["recap"] = tinymce.get('recap').getContent();
        reqBody["scale"] = $("#scale").val();
        
        $.ajax({
            type : type,
            contentType : "application/json",
            url : "assignments",    
            data : JSON.stringify(reqBody),
            timeout : 100000,
            success : function(response) {
                console.log("SUCCESS: ", response);
                if (this.props.edit) {
                    this.renderChild(this.props.assID);
                } else {
                    this.renderChild(response);
                }
            }.bind(this), 
            error : function(e) {
                console.log("ERROR: ", e);

                if (e.status == 500) {
                    if (this.props.edit) {
                        this.setState({errorMessage : "Failed to edit assignment. Contact support or try again later."});
                    } else {
                        this.setState({errorMessage: "Failed to create assignment. Contact support or try again later."});
                    }
                } else {
                    this.setState({errorMessage: e.responseJSON.errorMessage});
                }
            }.bind(this),
            done : function(e) {
                console.log("DONE");
            }
        });
    },
    renderChild : function (assignmentID) {
        ReactDOM.render(<NewAssignmentVideo edit={this.props.edit} courseID={this.props.courseID} assignmentID={assignmentID}/>, document.getElementById('courseContent'));
    },

    render : function() {
        if (this.props.edit) {
            var title = "EDIT ASSIGNMENT";
        } else {
            var title = "NEW ASSIGNMENT";
        }
      return (<div>
                <div key={new Date().getTime()} className="newAssForm">
                    <h3 className="contentTitle">{title}</h3>
                    <h3 className="errorMsg" id="newAssError">{this.state.errorMessage}</h3>
                    <input className="inputField" id="title" type="text" placeholder="title" />

                    <div id="dates">
                        <div className="DTContainer">
                            <p className="DTText">start:</p>
                            <input id="startDate" type="button" value="yyyy-mm-dd 00:00"/>
                        </div>

                        <div className="DTContainer">
                            <p className="DTText">submit by:</p>
                            <input id="endDate" type="button" value="yyyy-mm-dd 00:00"/>
                        </div>

                        <div className="DTContainer" id="rightDTContainer">
                            <p className="DTText">publish by:</p>
                            <input id="publish" type="button" value="yyyy-mm-dd 00:00"/>
                        </div>
                    </div>

                    <div id="settings">
                        <div className="DTContainer">
                            <p className="DTText">min-length of answer:</p>
                            <input id="minTimeSeconds" type="text" placeholder="seconds" />
                        </div>

                        <div className="DTContainer">
                            <p className="DTText">max-length of answer:</p>
                            <input id="maxTimeSeconds" type="text" placeholder="seconds" />
                        </div>

                        <div className="DTContainer" id="grading">
                            <p className="DTText">grading:</p>
                            <select id="scale">
                                <option value="_1_2_3_4_5">1,2,3,4,5</option>
                                <option value="IG_G_VG_MVG">IG,G,VG,MVG</option>
                                <option value="U_O_K_G">U,O,K,G</option>
                                <option value="F_E_D_C_B_A">F,E,D,C,B,A</option>
                                <option value="U_G">U,G</option>
                                <option value="U_3_4_5">U,3,4,5</option>
                            </select>
                        </div>
                    </div>

                    <div className="editorContainer">
                        <h5>Description of the assignment</h5>
                        <textarea className="inputField" id="description" type="text" placeholder="description" /><br/>
                    </div>

                    <div className="editorContainer">
                        <h5>Recap of the question - STUDENT WILL BE ABLE TO SEE THIS DURING RECORDING</h5>
                        <textarea className="inputField" id="recap" type="text" placeholder="recap" /><br/>
                    </div>

                    <div id="newAssButtons">
                        <div className="button primary-button SCButton" onClick = {handleCancel}> CANCEL </div>
                        <div className="button primary-button SCButton" id="post-question" onClick = {this.submitAssignment}> NEXT </div>
                    </div>
            </div>
        </div>)
    }
});



function handleCancel() {

}

var NewAssignmentVideo = React.createClass({
    getInitialState: function() {
        if (this.props.edit) {
            return {courseID : 0, assignmentID : 0, title : "EDIT ASSIGNMENT VIDEO"};
        } else {
            return {courseID : 0, assignmentID : 0, title : "NEW ASSIGNMENT VIDEO"};
        }
    },
    playVideo: function () {
        console.log("Video success");
    },
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();
        fd.append("video", blob);
        fd.append("courseID", this.props.courseID);
        return fd;
    },
    sendVideo : function(formData) {
        $.ajax({
            url: "assignments/" + this.props.assignmentID + "/video",
            type: 'POST',
            success: function (res) {
                console.log("Success");
                if(this.props.edit) {
                    ReactDOM.render(<NewAssignmentStatus message="Successfully edited a assignment"/>, document.getElementById('courseContent'));
                } else {
                    ReactDOM.render(<NewAssignmentStatus message="Successfully created a assignment"/>, document.getElementById('courseContent'));
                }

            }.bind(this),
            error: function (e) {
                this.deleteSettings(); 
                if (this.props.edit) {
                    ReactDOM.render(<NewAssignmentStatus message="Failed to edit assignment, contact support or try again later"/>,document.getElementById('courseContent'));
                } else {
                    ReactDOM.render(<NewAssignmentStatus message="Failed to create assignment, contact support or try again later"/>,document.getElementById('courseContent'));
                }

            }.bind(this),
            data: formData,
            cache: false,
            contentType: false,
            processData: false
        });
    },
    deleteSettings : function () {
        $.ajax({
            type : "DELETE",
            url : "assignments/" + this.props.assignmentID + "?courseID=" + this.props.courseID,
            timeout : 100000,
            success : function(response) {
                console.log("Removed settings");
            },
            error : function(e) {
                console.log(e);
                console.log("Failed to remove assignment. The assignment may have submissions or it doesn't exist");
            },
            done : function(e) {
                console.log("DONE");
            }
        });
    },
    render: function () {
        return (
            <div>
                <div className="newAssForm">
                    <h3 className="contentTitle">{this.state.title}</h3>
                    <div id="video-container" >
                        <Recorder id="recorder" playCallback={this.playVideo}
                                  postURL={"assignments/" + this.props.assignmentID + "/video"} formDataBuilder={this.formDataBuilder}
                                  recButtonID="record-question" stopButtonID="stop-question" fileName="assignmentVideo.webm" replay="true"
                                  postButtonID="post-video" httpCallback={this.sendVideo}/>
                        <button id="stop-question" className="recControls SCButton" disabled>Stop</button>
                        <button id="record-question" className="recControls SCButton">Record</button>
                    </div>
                    <div id="videoPageCtrls">
                        <button id="backBtn" className="recControls SCButton">Back</button>
                        <button id="post-video" className="recControls SCButton">Submit</button>
                    </div>
                </div>
            </div>
        )
    }
});

window.CourseContent = React.createClass({
    render: function() {
        var id = this.props.id;
        var type = this.props.type;
        var title;
        var content = [];
        switch(type){
            case "course":
                content.push(<h1>{type}</h1>);
                break;
            case "assignment":
                var courseId = this.props.course;
                var assignmentId = this.props.assignment;
                content.push(<h2>{type} (course={courseId})</h2>);
                content.push(<AssignmentContent course={courseId} assignment={assignmentId}/>)
                break;
            case "task":
                var courseId = this.props.course;
                var assignmentId = this.props.assignment;
                content.push(<h3>{type} (course={courseId}) (assignment={assignmentId})</h3>);
                break;
            default:
                content.push(<h4>{type}</h4>);
                break;
        }
        return (
            <div>
                <div className="newAssForm">
                    {content}
                </div>
            </div>
        );
    }
});

var NewAssignmentStatus = React.createClass({
    render : function() {
        return (
            <div>
                <h3 className="contentTitle">{this.props.message}</h3>
            </div>
        )
    }
});

//ReactDOM.render(<NewAssignmentVideo />, document.getElementById('courseContent'));
//ReactDOM.render(<NewAssignment />, document.getElementById('courseContent'));
window.NewAssignment = NewAssignment;
