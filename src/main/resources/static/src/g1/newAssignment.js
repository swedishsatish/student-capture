var NewAssignment = React.createClass({
    componentDidMount: function () {
        $("#startDate").datetimepicker(
            {
                dateFormat: "yy-mm-dd",
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
                /*
                 minDate
                 jQuery datepicker option
                 which set today date as minimum date
                 */
                minDate: 0
            });
    },
    submitAssignment: function() {
        var reqBody = {}
        reqBody["title"] = $("#title").val();
        reqBody["info"] = $("#info").val();
        reqBody["minTimeSeconds"] = $("#minTimeSeconds").val();
        reqBody["maxTimeSeconds"] = $("#maxTimeSeconds").val();
        reqBody["startDate"] = $("#startDate").val();
        reqBody["endDate"] = $("#endDate").val();
        reqBody["published"] = $("#publish").val();
        reqBody["recap"] = $("#recap").val();
        reqBody["scale"] = $("#scale").val();
        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "assignment",
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
                <div id="form">
                <input className="inputField" id="title" type="text" placeholder="title" /><br/>
                <input className="inputField" id="info" type="text" placeholder="description" /><br/>
                <input className="inputField" id="recap" type="text" placeholder="recap" /><br/>

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
                <div className="button primary-button" onClick = {handleCancel}> CANCEL </div>
                <div className="button primary-button" id="post-question" onClick = {this.submitAssignment}> SUBMIT </div>

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
                          postURL="/assignmentVideo" formDataBuilder={this.formDataBuilder}
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
