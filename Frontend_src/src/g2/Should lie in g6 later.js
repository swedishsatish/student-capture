/**
 * Created by victor on 2016-05-11.
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
        this.setState({value: event.target.value});
        console.log("hej");
    },


    render: function() {
        var list = this.props.list.map(function (item) {
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

var dropDownScale = 'U';
var DropDown2 = React.createClass({

    getInitialState: function() {
        return {
            value: 'U'
        }
    },

    change: function(event) {
        var indexNum = 0;
        var res = event.target.value.split(",");
        console.log(res);
        ReactDOM.render(<DropDown list={res}/>, document.getElementById('dropDownGrade'));

        dropDownScale = event.target.value;
        this.setState({value: event.target.value});

    },


    render: function() {

        return(
            <div id="dropdown2">
                <label for="dropDownMenu">Grade</label>
                <select id="dropDownMenu" onChange={this.change} value={this.state.value}>
                    <option value="1,2,3,4,5">1,2,3,4,5</option>
                    <option value="O,U,K,G">O,U,K,G</option>
                    <option value="U,G,VG,MVG">U,G,VG,MVG</option>
                </select>
            </div>
        )
    }
});