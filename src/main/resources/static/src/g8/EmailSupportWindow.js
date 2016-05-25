/**
 *
 *@author: Benjamin Björklund <c13bbd>,
 *         Tobias Estefors <dv13tes>
 *         Squad 8
 *
 *@note: This react-class is rendered from HelpWindow.js
 **/

/*
Taken from profile.js
 */
function genScripts(){
    var script1 = document.createElement("script");
    script1.setAttribute("src","src/g2/classie.js");
    script1.setAttribute("id","script1");
    document.body.appendChild(script1);
    var script2 = document.createElement("script");
    script2.setAttribute("id","script2");
    script2.setAttribute("src","src/g2/modalEffects.js");
    document.body.appendChild(script2);

}

/*
Taken from profile.js
 */
function reloadScripts() {
    var script1 = document.getElementById("script1");
    var script2 = document.getElementById("script2");
    document.body.removeChild(script1);
    document.body.removeChild(script2);
    genScripts();
}

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
        reloadScripts();
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
