/**
 * Created by Ludvig on 2016-05-18.
 */



window.EditCourse = React.createClass({
    componentDidMount: function () {

        tinymce.init({ selector:'#course-description',
            height: 350});

        console.log(this.props.course);
        console.log(this.props.course.courseDescription);
        if(this.props.course.active){
            $('#course-active').prop('checked', 'checked');
        }

        tinymce.get('course-description').setContent(this.props.course.courseDescription);
    },
    componentWillReceiveProps: function (nextProps) {
       
        if(nextProps.course.active){
            $('#course-active').prop('checked', 'checked');
        }

        tinymce.get('course-description').setContent(nextProps.course.courseDescription);
    },
    componentWillUnmount: function () {
        console.log("leaving");
        tinymce.get('course-description').setContent('');
        tinymce.EditorManager.editors = [];
    },
    handleClick: function (courseID,event) {

        var course = {

            year: parseInt($("#course-year").val(),10),
            term: $("#course-term").val(),
            courseName: $("#course-name").val(),
            courseDescription: tinymce.get('course-description').getContent(),
            active: $("#course-active").is(":checked")
        }

        $.ajax({
            type : "PUT",
            contentType : "application/json",
            url : window.globalURL + "/course/" + courseID,
            data : JSON.stringify(course),
            timeout : 100000,
            success : function(res) {
                if(res){
                    alert("Course edited");
                    //goto course content.
                }
                else {
                    alert("Failed to edit course");
                }
            }, error : function(e) {
                console.log("ERROR: ", e);
            }, done : function(e) {
                console.log("DONE");
            }
        });

        console.log(JSON.stringify(course))


    },
    changed: function () {

    },
    render: function () {
        var course = this.props.course;

        console.log(this.props.course);
        return (
            <div>
                <h3>Edit course information</h3>
                <form id="form">
                    <input type="text" id="course-name" placeholder="Course Name" value={course.courseName} onChange={this.changed}/>
                    <br />
                    <input type="text" id="course-year" placeholder="Course year" value={course.year} onChange={this.changed}/>
                    <br />
                    <input type="text" id="course-term" placeholder="Course term" value={course.term} onChange={this.changed}/>
                    <br />

                    <input type="checkbox" id="course-active"/>Active Course
                    <br />
                    <h5>Course Description</h5>
                    <textarea className="tinymceArea" id="course-description"></textarea>
                </form>
                <button onClick={this.handleClick.bind(this,course.courseId)}>Edit</button>
            </div>


        );
    }
});