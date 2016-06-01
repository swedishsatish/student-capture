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
        this.getData(this.props.assignment,this.props.user);
    },
    getData: function(assignmentid,userid) {
        $.ajax({
            type: "GET",
            url: "assignments/" + assignmentid + "/submissions/" + userid,
            timeout: 100000
        }).done(function (data) {
            this.setState({data: data});
        }.bind(this));
    },
    componentWillReceiveProps: function(nextProps) {
        this.getData(nextProps.assignment,nextProps.user);
        this.setState({source:null});
    },
    handleVideoClick: function () {
        var assignment = this.props.assignment;
        var user = this.props.user;
        var sour = "assignments/" + assignment + "/submissions/" + user + "/videos" + "/feedback.webm";
        this.setState({source: sour});
    },
    render: function () {

        if (this.state.data) {

            var response = this.state.data;

            response.submissionDate = new Date((response
                .submissionDate));

            var videoButContent;

            if(!this.state.source) {
                videoButContent = <div onClick={this.handleVideoClick} className="button primary-button SCButton">Get Video</div>;
            } else {
                videoButContent = <div><video width="720" height="460" src={this.state.source} preload="auto" controls/></div>;
            }

            return (
                <div>
                    <h5>Grade: {response.grade.grade}</h5>
                    <h5>Submission date: {response.submissionDate.toGMTString()}</h5>
                    <h5>Teacher name: {response.teacherName}</h5>
                    <h5>Feedback: {response.feedback}</h5>
                    <br />
                    {videoButContent}
                </div>
            )
        }

        return (
            <div>Loading...</div>
        )

    }
});