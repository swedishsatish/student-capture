/**
 *
 *@author: Tobias Estefors <dv13tes>
 **/

var TeacherViewSubmission=React.createClass({
    submissions:0,
    withdrawls:0,
    done:0,
    amount:0,


    componentDidMount: function() {


    },
    calculateSubmissions: function () {
        var userList=window.users;
        for(var i=0; i < userList.length;i++){
            console.log(userList[i].studentName.value)
            if(userList[i].withdraw){
                this.withdrawls++;
                continue;
            }
            this.submissions++;
        }
        this.done=this.submissions+this.withdrawls;
        this.amount=userList.length;
    },

    render:function () {
        this.calculateSubmissions();
        return (
            <div class="row">
                <div id="studentContainer">
                    <StudentList/>
                    <div id="textList">
                        <p>Submissions: {this.submissions}/{this.amount}</p>
                        <p>Withdrawl: {this.withdrawls}</p>
                        <p>Done: {this.done}/{this.amount}</p>
                    </div>
                </div>



            </div>
        )
    }


});

window.TeacherViewSubmission=TeacherViewSubmission;