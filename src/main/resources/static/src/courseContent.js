
var StartPage = React.createClass({
    render: function () {
        return <div>
            <h3>Welcome to Student Capture!</h3>
            <p>A video examination platform</p>
        </div>
    }

});



function submitAssignment() {
    var reqBody = {}
    reqBody["title"] = $("#title").val();
    reqBody["info"] = $("#info").val();
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
                var course = this.props.course;
                content.push(<h1>{type} {course["name"]}</h1>);
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


window.getQueryVariable = function (variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}

var qv = getQueryVariable("param")
if(qv == false){
    ReactDOM.render(<StartPage />, document.getElementById('courseContent'));
}
else {
    $.ajax({
        type : "POST",
        contentType : "application/json",
        url : "invite/" + qv,
        timeout : 100000,
        success : function(res) {
            //res.course
            console.log(res);
            ReactDOM.render(<CourseInfo course={res.course} />,document.getElementById('courseContent'));
            window.RenderMenu(res.course.courseId);
        }, error : function(e) {
            console.log("ERROR: ", e);
            console.log(e);
           
        }, done : function(e) {
            console.log("DONE");
        }
    });
}

