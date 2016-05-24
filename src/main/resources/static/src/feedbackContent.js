/**
 * Created by c14hht on 2016-04-28.
 *
 * Class that when entering the student id, assignment id and submitting gets the feedback and view it.
 *
 * This is to be changed when student id, assignment id  will be connected with the logged in person.
 *
 */

window.Feedback = React.createClass({

    getInitialState: function() {
        return {
            data: null,
            source: null
        };
    },
    componentDidMount: function() {
        $.ajax({
            type: "GET",
            url: "assignments/" + this.props.assignment + "/submissions/" + this.props.user,
            timeout: 100000
        }).done(function (data) {
            this.setState({data: data});
        }.bind(this));
    },
    handleVideoClick: function () {
        var assignment = this.props.assignment;
        var user = this.props.user;
        var sour = "assignments/" + assignment + "/submissions/" + user + "/video";
        this.setState({source: sour});
    },
    render: function () {

        if (this.state.data) {

            var response = this.state.data;

            response.submissionDate = new Date((response
                .submissionDate));

            var gradeColor;

            if (response.grade.grade === 'MVG') {
                gradeColor = 'green';
            } else if (response.grade.grade === 'IG') {
                gradeColor = 'red';
            }

            var videoButContent;

            if(!this.state.source) {
                videoButContent = <button onClick={this.handleVideoClick}>Get Video</button>;
            } else {
                videoButContent = <div><video width="720" height="460" src={this.state.source} preload="auto" controls/></div>;
            }

            return (
                <div>
                    <h5 style={{color:gradeColor}}>Grade: {response.grade
                        .grade}</h5>
                    <h5>Submission date: {response.submissionDate.toGMTString
                    ()}</h5>
                    <h5>Feedback: {response.feedback}</h5>
                    <h5>Teacher name: {response.teacherName}</h5>
                    <br />
                    {videoButContent}
                </div>
            )
        }

        return <div>Loading...</div>

    }
});