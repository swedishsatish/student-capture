var NewAssignment = React.createClass({
    render : function() {
      return <div>
            <form id="form" action="assignment" method="post">
                <h1>CREATE NEW ASSIGNMENT FOR COURSE (course code, course id)</h1>
                <input className="inputField" id="title" type="text" placeholder="title" /><br/>
                <textarea className="inputField" id="description" type="text" placeholder="description" /><br/>
                <p>VIDEO RECORDING COMPONENT GOES HERE</p>
                <textarea className="inputField" id="questionInformation" type="text" placeholder="question" /><br />
                <input id="startDate" type="datetime-local" /><br/>
                <input id="endDate" type="datetime-local" /><br/>
                <input id="minTimeSeconds" type="text" placeholder="minTimeSeconds" /><br/>
                <input id="maxTimeSeconds" type="text" placeholder="maxTimeSeconds" /><br/>
                <input id="isPublished" type="checkbox" value="Car"/>Publish Assignment<br/>
                <div className="button primary-button SCButton" onClick = {handleCancel}> CANCEL </div>
                <div className="button primary-button SCButton" onClick = {submitAssignment}> SUBMIT </div>
            </form>
        </div>
    }
});

function handleCancel() {

}

function submitAssignment() {
    var reqBody = {}
    reqBody["title"] = $("#title").val();
    reqBody["info"] = $("#description").val();
    reqBody["question"] = $("#questionInformation").val();
    reqBody["minTimeSeconds"] = $("#minTimeSeconds").val();
    reqBody["maxTimeSeconds"] = $("#maxTimeSeconds").val();
    reqBody["startDate"] = $("#startDate").val();
    reqBody["endDate"] = $("#endDate").val();
    reqBody["isPublished"] = $("#isPublished").is(':checked');
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "assignment",
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
}

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
window.NewAssignment = NewAssignment;
