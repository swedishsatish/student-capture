
/**
 * Checkbox that is used to determine if a student wants to share video recording or not.
 * @type {number} current state of passed
 */
var PermissionCheckBox = React.createClass({
    //Sets initial state for the checkbox to false.
    getInitialState: function() {
        return {

            checkBox: this.props.checkBox || false

        };
    },
    // Render function for CheckBox
    render: function() {
        return (

            <div id="permissionchecklabel">
                <label for="permissionchecklabel">
                    <input id="PermissionFromStudent" type="checkbox"
                           checkBox={this.state.checkBox}
                           onClick={this.handleClick}/>

                    Share!
                </label>
            </div>
        );
    },
    //Changes from checked to unchecked and vice-versa.
    handleClick: function(e) {

        this.setState({
            checkBox: e.target.checkBox
        });

    }
});

window.PermissionCheckBox=PermissionCheckBox;
