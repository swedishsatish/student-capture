/**
 * Created by sejiaw on 2016-05-10.
 */

/**
 * Drop down menu that lets the teacher choose the grade for the student.
 * @type {string} Sting with students selected grade
 */
// variable to keep track of grade, used in submission button
var dropDownGrade = 'U';
var DropDown = React.createClass({

    getInitialState: function() {
        return {
            value: 'U'
        }
    },

    change: function(event) {
        dropDownGrade = event.target.value;
        this.setState({value: event.target.value})
    },


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