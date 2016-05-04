var NewAssignment = React.createClass({
    render : function() {
      return <div>
                <form id="form" action="assignment" method="post">
                <input className="inputField" id="title" type="text" defaultValue="title" /><br/>
                <input className="inputField" id="info" type="text" defaultValue="description" /><br/>
                <p>VIDEO RECORDING COMPONENT GOES HERE</p>
                <input id="startDate" type="datetime-local" /><br/>
                <input id="endDate" type="datetime-local" /><br/>
                <input id="minTimeSeconds" type="text" defaultValue="minTimeSeconds" /><br/>
                <input id="maxTimeSeconds" type="text" defaultValue="maxTimeSeconds" /><br/>
                <input id="isPublished" type="checkbox" value="Car"/>Publish Assignment<br/>
                <div className="button primary-button" onClick = {handleCancel}> CANCEL </div>
                <div className="button primary-button" onClick = {submitAssignment}> SUBMIT </div>
            </form>
        </div>
    }
});

function handleCancel() {

}

function submitAssignment() {
    var ReturnMessage = React.createClass({
        render : function() {
            return (
                <div>{this.props.message} {this.props.response}</div>
            )
        }
    });
    var reqBody = {}
    reqBody["title"] = $("#title").val();
    reqBody["info"] = $("#info").val();
    reqBody["minTimeSeconds"] = $("#minTimeSeconds").val();
    reqBody["maxTimeSeconds"] = $("#maxTimeSeconds").val();
    reqBody["startDate"] = $("#startDate").val();
    reqBody["endDate"] = $("#endDate").val();
    reqBody["published"] = $("#isPublished").is(':checked');
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "assignment",
        data : JSON.stringify(reqBody),
        timeout : 100000,
        success : function(responseIn) {
            console.log("SUCCESS: ", responseIn);
            ReactDOM.render(<ReturnMessage message="Success: assignment ID is" response={responseIn}/>, document.getElementById('courseContent'));
        }, error : function(e) {
            console.log("ERROR: ", e);
            ReactDOM.render(<ReturnMessage message="ERROR:" reponse={reponseIn}/>, document.getElementById('courseContent'));
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
        switch(type){
            case "course":
                title = <h1>{type}</h1>
                break;
            case "assignment":
                title = <h2>{type}</h2>
                break;
            case "task":
                title = <h3>{type}</h3>
                break;
            default:
                title = <h4>{type}</h4>
                break;
        }
        return (
            <div>
                {title}
                ID: {id}
            </div>
        );
    }
});

ReactDOM.render(<NewAssignment />, document.getElementById('courseContent'));
