/**
 * Handles rendering for teacher feedback components.
 * @author: dv13trm, c14gls, group 6
 */

/**
 * Renders all the different built components for the GUI in prioritizing order.
 * For example: First it renders Recorder, then CommentBox and so on.
*/
 var RenderHandle = React.createClass({
    /**
     * Rendering all components for teacher feedback.
     * @returns {XML} components.
     */
    render: function () {
        return(
            <div class="row">
                <div id="blanket" style={{"display":"none"}}></div>
                <div id="popUpDiv" style={{"display":"none"}}></div>
                <TeacherRecordVideo/>
                <div id="feedBackContainer">
                    <div id="submissioncontainer">
                        <div id="commentbox">
                            <CommentBox />
                        </div>
                        <div id="gradeAndPassContainer">
                            <div id="passbox">
                                <PermissionCheckBox/>
                                <PassCheckBox />
                            </div>
                            <div id="dropDown">
                                <DropDown />
                            </div>
                        </div>
                    </div>
                    <div id="buttonContainer">
                        <div id="backButton">
                            <BackButton />
                        </div>
                        <div id="submitButton">
                            <SubmitButton student={this.props.student} />
                        </div>

                    </div>
                </div>
            </div>
        )
    }

});
window.RenderHandle = RenderHandle;
