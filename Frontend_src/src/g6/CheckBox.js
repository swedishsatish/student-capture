/**
 * Created by sejiaw on 2016-05-10.
 */


/**
 * Checkbox that is used to determine if a student passed an assignment or not.
 * @type {number} current state of passed
 */
var isPassed = 0;
var CheckBox = React.createClass({

    //Sets initial state for the checkbox to false.
    getInitialState: function() {
        return {
            checkBox: this.props.checkBox || false

        };
    },
    // Render function for CheckBox
    render: function() {

        return (
            <div id="checklabel">
                <label for="checklabel">
                    <input id="checkbox" type="checkbox"
                           checkBox={this.state.checkBox}
                           onClick={this.handleClick}/>
                    Passed!
                </label>
            </div>
        );
    },
    //Changes from checked to unchecked and vice-versa.
    handleClick: function(e) {

        if(isPassed == 0) {
            isPassed = 1;
        } else {

            isPassed = 0;
        }

        this.setState({
            checkBox: e.target.checkBox
        });

    }
});
window.CheckBox=CheckBox;