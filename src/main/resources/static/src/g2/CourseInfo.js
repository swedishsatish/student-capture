/**
 * Created by: Victor From & Jonas Nyman
 * Display course information when pressing on a course!!
 *
 */


window.CourseInfo = React.createClass({


    render: function () {
        var course = this.props.course;

        console.log(this.props.course);
        /* Prints the given course as html sent from dynamic menu
         * recieved from the database
         */
        return (
            <div dangerouslySetInnerHTML={course.courseDescription}>

            </div>


        );
    }
});