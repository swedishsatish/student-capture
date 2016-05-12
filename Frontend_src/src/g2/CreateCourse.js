/**
 * Created by Ludvig on 2016-05-12.
 */
window.CreateCourse = React.createClass({
    render: function () {
        return (
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
            </form>


        );
    }
});