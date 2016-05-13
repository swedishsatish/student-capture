var NewAssignment = React.createClass({
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();
        fd.append("videoName", fileName);
        fd.append("video", blob);
        return fd;
    },
    render : function() {
      return <div>
                <form id="form" action="assignment" method="post">
                <input className="inputField" id="title" type="text" defaultValue="title" /><br/>
                <input className="inputField" id="info" type="text" defaultValue="description" /><br/>
                <input className="inputField" id="recap" type="text" defaultValue="recap" /><br/>
                <Recorder playCallback={this.playVideo}
                          postURL="/video/textTest" formDataBuilder={this.formDataBuilder}
                          recButtonID="record-question" stopButtonID="stop-question" fileName="testVid.webm" replay="true"
                          postButtonID="post-question"
                />
                <div className="four columns u-pull-left">
                    <button id="record-question" className="recControls">Record</button>
                    <button id="stop-question" className="recControls" disabled>Stop</button>
                </div>
                <input id="startDate" type="button" value="yyyy-mm-dd 00:00"/>Start Date<br/>
                <input id="endDate" type="button" value="yyyy-mm-dd 00:00"/>End Date<br/>
                <input id="minTimeSeconds" type="number" defaultValue="minTimeSeconds" /><br/>
                <input id="maxTimeSeconds" type="number" defaultValue="maxTimeSeconds" /><br/>
                <input id="publish" type="button" value="yyyy-mm-dd 00:00"/>Publish Date<br/>
                    <select id="scale">
                        <option value="NUMBER_SCALE">1,2,3,4,5</option>
                        <option value="U_G_VG_MVG">U,G,VG,MVG</option>
                        <option value="U_O_K_G">U,O,K,G</option>
                    </select>Grade scale<br/>
                <div className="button primary-button" onClick = {handleCancel}> CANCEL </div>
                <div className="button primary-button" id="post-question" onClick = {submitAssignment}> SUBMIT </div>
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
    reqBody["scale"] = $("#scale").val();
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "assignment",
        data : JSON.stringify(reqBody),
        timeout : 100000,
        success : function(responseIn) {
            console.log("SUCCESS: ", responseIn);
            ReactDOM.render(<VideoComponent assignmentID={responseIn}/>, document.getElementById('courseContent'));
        }, error : function(jqxhr) {
            console.log("ERROR: ", jqxhr);
            ReactDOM.render(<ReturnMessage message="ERROR" reponse=""/>, document.getElementById('courseContent'));
       }, done : function(e) {
            console.log("DONE");
        }
    });
}

var VideoComponent = React.createClass ({
    render: function() {
        return (
            <div>
                <Recorder playCallback={this.playVideo}
                                 postURL="/uploadVideo/" formDataBuilder={this.formDataBuilder}
                                 recButtonID="record-question" stopButtonID="stop-question" fileName="testVid.webm" replay="true"
                                 postButtonID="post-question"
                />
                <div className="four columns u-pull-left">
                    <button id="record-question" className="recControls">Record</button>
                    <button id="stop-question" className="recControls" disabled>Stop</button>
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
