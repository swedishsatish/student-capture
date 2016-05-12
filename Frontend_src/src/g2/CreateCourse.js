/**
 * Created by Ludvig on 2016-05-12.
 */
window.CreateCourse = React.createClass({
    componentDidMount: function () {
        tinymce.init({ selector:'textarea.tinymceArea',
            height: 350});
    },
    render: function () {


        return (
            <div>
                <h3>Create new course</h3>
                <form id="form">
                    <input type="text" placeholder="Course Name"/>
                    <br />
                    <input type="text" placeholder="Course year"/>
                    <br />
                    <input type="text" placeholder="Course term"/>
                    <br />
                    <input type="text" placeholder="Course code"/>
                    <br />
                    <input type="checkbox"/>Active Course
                    <br />
                    <h5>Course Description</h5>
                    <textarea className="tinymceArea"></textarea>
                </form>
            </div>


        );
    }
});