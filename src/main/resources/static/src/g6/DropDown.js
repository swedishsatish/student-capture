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
            value: '*'
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
     * Render function for dropdown menu. Using the props(gradescale)
     * to generate correct scale.
     * @returns {XML} A dropdown menu with values.
     */
    render: function() {
        console.log(this.props.scale);

        var tempList = this.props.scale.split("_");

        var list = tempList.map(function (item) {
            return <option value={item}>{item}</option>
        })

        return(
            <div id="dropdown">
                <label for="dropDownMenu">Grade</label>
                <select id="dropDownMenu" onChange={this.change} value={this.state.value}>
                    {list}
                </select>
            </div>
        )
    }
});
window.DropDown=DropDown;