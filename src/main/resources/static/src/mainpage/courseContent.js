
var StartPage = React.createClass({
    render: function () {
        return <div>
            <h3>Welcome to Student Capture!</h3>
            <p>A video examination platform</p>
        </div>
    }

});


/*
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

*/

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

