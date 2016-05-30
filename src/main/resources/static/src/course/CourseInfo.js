/**
 * Created by: Victor From & Jonas Nyman
 * Display course information when pressing on a course!!
 *
 */

function getCourseHash(courseID) {
    $.ajax({
        type : "GET",
        contentType : "application/json",
        url : "invite/" + courseID,
        timeout : 100000,
        success : function(res) {
            
            document.getElementById("courseHashDiv").innerHTML = "Course url: " +
                                                        window.location.protocol + "//" +
                                                        window.location.host +
                                                        window.location.pathname + "?param="+  res.hex;
        }, error : function(e) {
            console.log("ERROR: ", e);
        }, done : function(e) {
            console.log("DONE");
        }
    });
}

window.CourseInfo = React.createClass({
    componentDidMount: function () {
        document.getElementById("course").innerHTML = this.props.course.courseDescription;
        if(this.props.role == "teacher") {
            getCourseHash(this.props.course.courseId);
        }
        else {
            document.getElementById("courseHashDiv").innerHTML = "";
        }
    },
    componentWillReceiveProps: function (nextProps) {
        document.getElementById("course").innerHTML = nextProps.course.courseDescription;
        if(nextProps.role == "teacher") {
            getCourseHash(nextProps.course.courseId);
        }
        else {
            document.getElementById("courseHashDiv").innerHTML = "";
        }
    },
    render: function () {
        var course = this.props.course;

        console.log(this.props.course);
        /* Prints the given course as html sent from dynamic menu
         * recieved from the database
         */
        return (
            <div>
                <div id="courseHashDiv">


                </div>
                <div id="course" className="courseInfo">
                
                </div>
            </div>

        );
    }
});


