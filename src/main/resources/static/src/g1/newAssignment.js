var NewAssignment = React.createClass({
    componentDidMount: function () {
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
    },
    submitAssignment: function() {
        var reqBody = {};
        var videoIntervall = {};
        var assignmentIntervall = {};


        videoIntervall["minTimeSeconds"] = $("#minTimeSeconds").val();
        videoIntervall["maxTimeSeconds"] = $("#maxTimeSeconds").val();
        assignmentIntervall["startDate"] = $("#startDate").val();
        assignmentIntervall["endDate"] = $("#endDate").val();
        assignmentIntervall["publishedDate"] = $("#publish").val();
        reqBody["courseID"] = "1200";
        reqBody["title"] = $("#title").val();
        reqBody["description"] = $("#description").val();
        reqBody["videoIntervall"] = videoIntervall;
        reqBody["assignmentIntervall"] = assignmentIntervall;
        reqBody["recap"] = $("#recap").val();
        reqBody["scale"] = $("#scale").val();
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "assignments",
            data : JSON.stringify(reqBody),
            timeout : 100000,
            success : function(response) {
                console.log("SUCCESS: ", response);
                ReactDOM.render(<NewAssignmentVideo assignmentID={response}/>, document.getElementById('courseContent'));
            }, error : function(e) {
                console.log("ERROR: ", e);
            }, done : function(e) {
                console.log("DONE");
            }
        });
    },
    render : function() {
      return <div>
                <div id="newAssForm">
                <input className="inputField" id="title" type="text" placeholder="title" /><br/>
                <textarea className="inputField" id="description" type="text" placeholder="description" /><br/>
                <textarea className="inputField" id="recap" type="text" placeholder="recap" /><br/>

                <input id="startDate" type="button" value="yyyy-mm-dd 00:00"/>Start Date<br/>
                <input id="endDate" type="button" value="yyyy-mm-dd 00:00"/>End Date<br/>
                <input id="minTimeSeconds" type="text" placeholder="minTimeSeconds" /><br/>
                <input id="maxTimeSeconds" type="text" placeholder="maxTimeSeconds" /><br/>
                    <input id="publish" type="button" value="yyyy-mm-dd 00:00"/>Publish Date<br/>
                    <select id="scale">
                        <option value="NUMBER_SCALE">1,2,3,4,5</option>
                        <option value="U_G_VG_MVG">U,G,VG,MVG</option>
                        <option value="U_O_K_G">U,O,K,G</option>
                    </select>Grade scale<br/>
                <div className="button primary-button SCButton" onClick = {handleCancel}> CANCEL </div>
                <div className="button primary-button SCButton" id="post-question" onClick = {this.submitAssignment}> SUBMIT </div>

            </div>
        </div>
    }
});



function handleCancel() {

}

var NewAssignmentVideo = React.createClass({
    playVideo: function () {
        console.log("Video success");
    },
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();
        fd.append("video", blob);
        fd.append("courseID", 1200);
        fd.append("assignmentID", this.props.assignmentID);
        return fd;
    },
    render: function () {
        return (
            <div>
                <Recorder id="recorder" playCallback={this.playVideo}
                          postURL="/assignments/video" formDataBuilder={this.formDataBuilder}
                          recButtonID="record-question" stopButtonID="stop-question" fileName="assignmentVideo.webm" replay="true"
                          postButtonID="post-video"
                />
                <button id="record-question" className="recControls">Record</button>
                <button id="stop-question" className="recControls" disabled>Stop</button>
                <button id="post-video" className="recControls">Post Video</button><br/>
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
            <div>{content}</div>
        );
    }
});

ReactDOM.render(<NewAssignment />, document.getElementById('courseContent'));
window.NewAssignment = NewAssignment;
