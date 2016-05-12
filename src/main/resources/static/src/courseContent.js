var NewAssignment = React.createClass({
    render : function() {
      return <div>
                <form id="form" action="assignment" method="post">
                <input className="inputField" id="title" type="text" defaultValue="title" /><br/>
                <input className="inputField" id="info" type="text" defaultValue="description" /><br/>
                    <input className="inputField" id="recap" type="text" defaultValue="recap" /><br/>
                    
                <p>VIDEO RECORDING COMPONENT GOES HERE</p>
                <input id="startDate" type="button" value="yyyy-mm-dd 00:00"/>Start Date<br/>
                <input id="endDate" type="button" value="yyyy-mm-dd 00:00"/>End Date<br/>
                <input id="minTimeSeconds" type="number" defaultValue="minTimeSeconds" /><br/>
                <input id="maxTimeSeconds" type="number" defaultValue="maxTimeSeconds" /><br/>
                <input id="publish" type="button" value="yyyy-mm-dd 00:00"/>Publish Date<br/>

                <div className="button primary-button" onClick = {handleCancel}> CANCEL </div>
                <div className="button primary-button" onClick = {submitAssignment}> SUBMIT </div>
            </form>
        </div>
    },

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
    reqBody["published"] = $("#publish").val();
    reqBody["recap"] = $("#recap").val();
    reqBody["scale"] = "NUMBER_SCALE";
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "assignment",
        data : JSON.stringify(reqBody),
        timeout : 100000,
        success : function(responseIn) {
            console.log("SUCCESS: ", responseIn);
            ReactDOM.render(<ReturnMessage message="Success: assignment ID is" response={responseIn}/>, document.getElementById('courseContent'));
        }, error : function(jqxhr) {
            console.log("ERROR: ", jqxhr);
            ReactDOM.render(<ReturnMessage message="ERROR" reponse=""/>, document.getElementById('courseContent'));
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
