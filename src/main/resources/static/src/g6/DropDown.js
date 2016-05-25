/**
 * Drop down menu that will be used to select student grade.
 * @author: dv13trm, c14gls, group 6
 */
var DropDown = React.createClass({
    /**
     * Set initial state for drop down to grade U
     * @returns {{value: string}} value U to dropdown.
     */
    getInitialState: function() {
        return {
            value: 'U'
        }
    },
    /**
     * Set value to entered event.
     * @param event change that happened to drop down, ex. if changed from grade U to G.
     */
    change: function(event) {
        this.setState({value: event.target.value})
    },
    /**
     * Render function for dropdown menu.
     * @returns {XML} A dropdown menu with values.
     */
    render: function() {
        console.log(this.props.scale);
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