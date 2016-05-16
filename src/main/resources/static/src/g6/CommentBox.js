/**
 * Created by sejiaw on 2016-05-10.
 */
/**
 * Created by sejiaw on 2016-05-10.
 */

var hasEdited = false;
/**
 * Creates the comment box used by the GUI.
 * The comment box is used by the teacher to submit feedback to the student.
 */
var CommentBox = React.createClass({

    getInitialState: function () {
        return {currentValue: this.props.children};
    },
    handleChange: function(event) {
        this.setState({value: event.target.value});
    },
    // Upon clicking inside the textField this function sets the text inside to empty.
    onClick: function (event) {
        if(!hasEdited){
            this.setState({value: ''});
            hasEdited=true;
        }
    },

    // The render function for the component CommentBox
    render: function () {
        return (
            <textarea placeholder="Write comments regarding feedback here..." id="teachercomments"
                      value={this.state.value} onChange={this.handleChange} onClick={this.onClick} />

        );
    }

});

window.CommentBox=CommentBox;