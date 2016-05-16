/**
 * Checkbox to check if student have passed a assignment or not,
 * will be used for teacher feedback
 * @author: dv13trm, c14gls, group 6
 */


/**
 * Checkbox that is used to determine if a student passed an assignment or not.
 * @type {number} current state of passed
 */
var CheckBox = React.createClass({
    //Sets initial state for the checkbox to false.
    getInitialState: function() {
        return {

            checkBox: this.props.checkBox || false

        };
    },
    /**
     * Render function for checkbox
     * @returns {XML} A checkbox.
     */
    render: function() {
        return (

            <div id="checklabel">
                <label for="checklabel">
                    <input id="ifStudentPass" type="checkbox"
                           checkBox={this.state.checkBox}
                           onClick={this.handleClick}/>

                    Passed!
                </label>

            </div>
        );
    },
    /**
     * Changes state for checkbox.
     * @param e event for change.
     */
    handleClick: function(e) {

        this.setState({
            checkBox: e.target.checkBox
        });

    }
});

window.CheckBox=CheckBox;
