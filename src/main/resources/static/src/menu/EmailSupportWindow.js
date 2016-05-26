/**
 *
 *@author: Benjamin Björklund <c13bbd>,
 *         Tobias Estefors <dv13tes>
 *         Squad 8
 *
 *@note: This react-class is rendered from HelpWindow.js
 **/



/**
 * Window for the user to send an email to support.
 */
window.EmailSupportWindow = React.createClass({

    /**
     * Sends the email to support (via the resource-layer). This should
     * send data to HelpResource.java
     */
    sendClickHandle: function () {
        $.ajax({
            url: "help/form",
            type: "PUT",
            async: false,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                //Get the given return email address
                "senderEmail": document.getElementById('emailTextArea').value + "",
                //Get the type of operating system
                "senderOperatingSystem": navigator.platform + "", 
                //Get the browser name and version
                "senderBrowser": DetectRTC.browser.name + " " + DetectRTC.browser.version, 
                //Get the message written by user
                "senderMessage": document.getElementById('messageTextArea').value + "" 
            })
        });

        document.getElementById('sendButton').disabled = true;
    },
    /**
     * Handle for if the user presses the 'back'.
     */
    backClickHandle: function () {
        ReactDOM.render(<HelpWindow />, document.getElementById('modal-container'));
        window.reloadScripts();
    },
    render: function () {
        return (
            <div>
                <div className="row">
                    <b>How can we help?</b> <br />
                    Your email: <input id="emailTextArea" type="text" placeholder="example@example.com" /> <br />
                    Message: <br />
                    <textarea id="messageTextArea" placeholder="Write message here"/> <br />
                </div>

                <div className="row">
                    <div className="two columns u-pull-left">
                        <button id="sendButton" onClick={this.sendClickHandle}>Send</button>
                    </div>
                    <div className="two columns u-pull-right">
                        <button className="md-close">Close</button>
                        <button onClick={this.backClickHandle}>Back</button>
                    </div>
                </div>
            </div>
        );
    }

});
