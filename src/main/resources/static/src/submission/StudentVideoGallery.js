/* StudentVideoGallery
* presents all public submissions (videos) on an assignment users
* in Submission Gallery menu-item
*/


var StudentVideoGallery = React.createClass({

    submissionsArray: null,

    componentWillMount: function() {
            $.ajax({
                url: "assignments/"+this.props.assID+"/submissions/",
                type: "GET",
                async: false,
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
                var submissionURL = "assignments/"+assID+"/submissions/"+value.studentID+"/videos/submission";
                if (value.status == "answer") {
                    i++;
                    return <div className="videogalleryitem" key={i}>{value.firstName + " "+value.lastName}<br/><video controls src={submissionURL}></video></div>;
                }
            }
        });

        return  <div><h2>Submission Gallery</h2><br/><div className="videogallery">{ namesList }</div></div>
    }




});
window.StudentVideoGallery = StudentVideoGallery;

