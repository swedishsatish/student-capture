/**
 *
 *@author: Benjamin Björklund <c13bbd>,
 *         Tobias Estefors <dv13tes>
 *
 *@note: This react-class is rendered from HelpWindow.js
 **/

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

function reloadScripts() {
    var script1 = document.getElementById("script1");
    var script2 = document.getElementById("script2");
    document.body.removeChild(script1);
    document.body.removeChild(script2);
    genScripts();
}

window.EmailSupportWindow = React.createClass({

    sendClickHandle: function () {
        $.ajax({
            url: "help/form",
            type: "PUT",
            async: false,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                "senderEmail": document.getElementById('emailTextArea').value + "",
                "senderOperatingSystem": document.getElementById('platformSelect').value + "",
                "senderBrowser": document.getElementById('browserSelect').value + "",
                "senderMessage": document.getElementById('messageTextArea').value + ""
            }),
            success: function (data, status) {

            }.bind(this),
            error: function (xhr, status, err) {

            }.bind(this)
        });

        document.getElementById('sendButton').disabled = true;
    },
    backClickHandle: function () {
        ReactDOM.render(<HelpWindow />, document.getElementById('modal-container'));
        reloadScripts();
    },
    render: function () {
        return (
            <div>
                <div className="row">
                    How can we help? <br />
                    Your email: <input id="emailTextArea" type="text" placeholder="example@example.com" /> <br />
                    Platform:
                    <select id="platformSelect">
                        <option>Windows ME</option>
                        <option>Redhat Fedora</option>
                    </select> <br />
                    Web-browser:
                    <select id="browserSelect">
                        <option>Lynx</option>
                        <option>Chromium</option>
                    </select> <br />
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
