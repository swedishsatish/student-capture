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
        return { data: null };
    },
    componentDidMount: function() {
        //Skicka till assignments/{assignmentid}/submissions/{studentid}
        $.ajax({
            type: "GET",
            url: "assignments/" + this.props.assignment + "/submissions/" + this.props.user,
            timeout: 100000
        }).done(function (data) {
            this.setState({data: data});
        }.bind(this));
    },
    handleVideoClick: function () {
        $.ajax({
            type: "GET",
            url: "assignments/" + this.props.assignment + "/submissions/" + this.props.user + "/video",
            timeout: 100000,
            success: function (data) {
                render(
                    <div>loading...</div>
                )
            },
            error: function (err) {

            }
        });
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

            return (
                <div>
                    <h5 style={{color:gradeColor}}>Grade: {response.grade
                        .grade}</h5>
                    <h5>Submissiondate: {response.submissionDate.toGMTString()}</h5>
                    <h5>Feedback: {response.feedback}</h5>
                    <h5>Teachername: {response.teacherName}</h5>
                    <br />
                    <button onClick={this.handleVideoClick}>Get Video</button>
                </div>
            )
        }

        return <div>Loading...</div>

    }
});