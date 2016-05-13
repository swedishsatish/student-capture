/**
 * Created by Ludvig on 2016-05-12.
 */

var generateCourseID = function () {
    return Math.floor((Math.random() * 1000000000) + 1);
}

var callback = function (res,uid) {

    if(res){
        alert("Course Added");
        RenderMenu(uid);
    }
    else {
        alert("Failed to add course");
    }
}

window.CreateCourse = React.createClass({
    componentDidMount: function () {
        tinymce.init({ selector:'textarea.tinymceArea',
            height: 350});
    },
    handleClick: function (uid,event) {
        var fd = new FormData();
        fd.append("courseID",generateCourseID());
        fd.append("courseCode",$("#course-code").val());
        fd.append("year",$("#course-year").val());
        fd.append("term",$("#course-term").val());
        fd.append("courseName",$("#course-name").val());
        fd.append("courseDescription",tinymce.get('course-description').getContent());
        fd.append("active",$("#course-active").val());
        fd.append("userID",uid);

        var request = new XMLHttpRequest();
        request.onreadystatechange = function () {
            if (request.readyState == 4 && request.status == 200) {
                callback(request.responseText,uid);
            }

        };

        request.onload = function(){

            if(request.status == 404) {

                alert("Upload failed, no server connection.");
            }
            else if(request.status == 408) {

                alert("Connection timed out.");
            }


        }
        
        request.open('POST', window.globalURL + "/DB/addCourseWithTeacher",true);

        request.send(fd);

    },
    render: function () {

        console.log(this.props);
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
                    <input type="text" id="course-code" placeholder="Course code"/>
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