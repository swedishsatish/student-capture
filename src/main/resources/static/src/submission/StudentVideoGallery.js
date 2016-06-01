/* StudentVideoGallery
* presents all public submissions (videos) on an assignment users
* in Submission Gallery menu-item
*/


var StudentVideoGallery = React.createClass({

    submissionsArray: null,

    componentWillMount: function() {
            $.ajax({
                url: "assignments/"+this.props.assID+"/submissions/?&permission=true",
                type: "GET",
                async: true,
                success: function (data, status) {
                    this.submissionsArray = data;
                }.bind(this),
                error: function (xhr, status, err) {
                }.bind(this)
            });
    },
    render: function () {
        var assID = this.props.assID;
        var userID = this.props.userID;
        var i = -1;
        var namesList = this.submissionsArray.map(function(value){
            if (value.studentPublishConsent) {
                var submissionURL = "assignments/"+assID+"/submissions/"+value.studentID+"/videos/submission/?&permission=true";
                if (value.status == "answer") {
                    i++;
                    return <div className="videogalleryitem" key={Math.random()}>{value.firstName + " "+value.lastName}<br/><video controls src={submissionURL}></video></div>;
                }
            }
        });

        return  <div key={Math.random()}><h2>Submission Gallery</h2><br/><div className="videogallery" key={Math.random()}>{ namesList }</div></div>
    }




});
window.StudentVideoGallery = StudentVideoGallery;

