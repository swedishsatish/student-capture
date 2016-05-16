/**
 * Drop down menu that will be used to select student grade.
 * @author: dv13trm, c14gls, group 6
 */

/**
 * Drop down menu that lets the teacher choose the grade for the student.
 *
 */
var DropDown = React.createClass({
    /**
     * Set initialstate for drop down to U
     * @returns {{value: string}} value U to dropdown.
     */
    getInitialState: function() {
        return {
            value: 'U'
        }
    },
    /**
     * Set value to entered event.
     * @param event change that happend to drop down, ex. if changed from U to G.
     */
    change: function(event) {
        this.setState({value: event.target.value})
    },
    /**
     * Render function for dropdown menu.
     * @returns {XML} A dropdown menu with values.
     */
    render: function() {
        return(
            <div id="dropdown">
                <label for="dropDownMenu">Grade</label>
                <select id="dropDownMenu" onChange={this.change} value={this.state.value}>
                    <option value="U">U</option>
                    <option value="O">O</option>
                    <option value="K">K</option>
                    <option value="G">G</option>
                </select>
            </div>
        )
    }
});
window.DropDown=DropDown;