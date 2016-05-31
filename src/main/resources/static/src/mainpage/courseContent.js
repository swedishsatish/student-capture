/**
 * start page content
 */
var StartPage = React.createClass({
    render: function () {
        return <div>
            <h3>Welcome to Student Capture!</h3>
            <p>A video examination platform</p>
        </div>
    }

});

/**
 * get variables from url
 * @param variable
 * @returns {*}
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

/**
 * if queryvariable param dont exist, render start page.
 * otherwise go to specific course.
 *
 */
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

