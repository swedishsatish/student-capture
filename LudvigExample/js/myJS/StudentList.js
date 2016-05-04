/**
 * Created by c13lbm on 4/26/16.
 */



var StudentList = React.createClass ({

    componentDidMount: function() {
        var newTableObject = document.getElementById("students-table");
        console.log(1);
        sorttable.makeSortable(newTableObject);
    },

    clickhandle: function (event) {
        var temp = {
            'userID' : "John",
            'courseID' : 232,
            'videoName' : "lolNoob"
        };

        //console.log(JSON.stringify(temp));
        $.get( "https://human:8443/video/textTest", {userID: JSON.stringify(temp)}, function (data) {
            console.log("succ" + data);
        } );
    },

    render: function (){
        return (
            <div className="four columns">
                <table className="u-full-width sortable" id="students-table">
                    <thead>
                    <tr >
                        <th>Name</th>
                        <th>Task</th>
                        <th>Sex</th>
                        <th>Location</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr onClick={this.clickhandle}>
                        <td>Dave Gamache</td>
                        <td>26</td>
                        <td>Male</td>
                        <td>San Francisco</td>
                    </tr>
                    <tr>
                        <td>Dwayne Johnson</td>
                        <td>42</td>
                        <td>Male</td>
                        <td>Hayward</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        );
    }
});

window.StudentList = StudentList;