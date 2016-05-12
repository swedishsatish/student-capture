/**
 * Created by sejiaw on 2016-05-10.
 */


/**
 * Renders all the different built components for the GUI in prioritizing order.
 * For example: First it renders Recorder, then CommentBox and so on.
*/
 var RenderHandle = React.createClass({
    render: function () {
        return(
            <div class="row">
                <div id="blanket" style="display:none;"></div>
                <div id="popUpDiv" style="display:none;"></div>
                <div id="recorder">
                    <Recorder />
                </div>
                <div id="feedBackContainer">
                    <div id="submissioncontainer">
                        <div id="commentbox">
                            <CommentBox />
                        </div>
                        <div id="gradeAndPassContainer">
                            <div id="passbox">
                                <CheckBox />
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
                            <SubmitButton />
                        </div>

                    </div>
                </div>
            </div>
        )
    }

});

window.RenderHandle = RenderHandle;
