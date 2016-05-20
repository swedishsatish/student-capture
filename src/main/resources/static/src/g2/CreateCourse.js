/**
 * Created by Ludvig on 2016-05-12.
 */

/**
 * generates random course id, ONLY TEMPORARY will be redundant
 * @returns {number}
 */
var generateCourseID = function () {
    return Math.floor((Math.random() * 1000000000) + 1);
}


window.CreateCourse = React.createClass({
    /**
     * When rendered initiate tinymce.
     */
    componentDidMount: function () {
        tinymce.init({ selector:'textarea.tinymceArea',
            height: 350});
    },
    /**
     * When content will unrender remove tinymce from textarea.
     */
    componentWillUnmount: function () {
        tinymce.get('course-description').setContent('');
        tinymce.EditorManager.editors = [];
    },
    /**
     * Creates course object and post it as json to the server
     * @param uid
     * @param event
     */
    handleClick: function (uid,event) {

        var course = {
            courseId: generateCourseID(),
            year: parseInt($("#course-year").val(),10),
            term: $("#course-term").val(),
            courseName: $("#course-name").val(),
            courseDescription: tinymce.get('course-description').getContent(),
            active: $("#course-active").is(":checked"),
            initialTeacherId:uid
        }

        $.ajax({
            type : "POST",
            contentType : "application/json",
            url : "course",
            data : JSON.stringify(course),
            timeout : 100000,
            success : function(res) {
                RenderMenu(uid);
                $.get("course/" + res.courseId,function (res2) {

                    ReactDOM.render(<CourseInfo course={res2}/>,document.getElementById("courseContent"));


                });


            }, error : function(e) {
                console.log("ERROR: ", e);
            }, done : function(e) {
                console.log("DONE");
            }
        });




    },
    /**
     * Render the create input forms
     * @returns {XML}
     */
    render: function () {

        
        return (
            <div>
                <h3>Create new course</h3>
                <form id="form">
                    <input type="text" id="course-name" placeholder="Course Name"/>
                    <br />
                    <input type="text" id="course-year" placeholder="Course year"/>
                    <br />
                    <input type="text" id="course-term" placeholder="Course term"/>
                    <br />

                    <input type="checkbox" id="course-active"/>Active Course
                    <br />
                    <h5>Course Description</h5>
                    <textarea className="tinymceArea" id="course-description"></textarea>
                </form>
                <button onClick={this.handleClick.bind(this,this.props.uid)}>Create</button>
            </div>


        );
    }
});