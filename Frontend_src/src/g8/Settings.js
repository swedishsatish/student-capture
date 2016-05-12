/**
 * Made by Andreas Savva and Benjamin Björklund, Squad 8
 *
 * This is a settings menu for the user interface.
 */

window.Settings = React.createClass({

    /*
    These attributes are currently not used.
     */
    textSize: 12,
    emailAddress: "",
    language: "",

    /*
    Switches state of the input field for the email address to be
    disabled or not disabled.
     */
    changeEmailInputState: function () {
        var emailInputStateDisabled = document.getElementById("emailAddressInput");

        emailInputStateDisabled.disabled = !emailInputStateDisabled.disabled;
    },

    /*
    When the user presses "Save" to save their settings, this function will
    send a HTTP POST request to save the settings in the database.
     */
    sendPOST: function () {
        NotificationManager.success('YAY', 'this is awesome');
        $.post(
            window.globalURL + "/settings",
            {
                userID: this.props.profile.name,
                language: document.getElementById("languageSelect").value,
                emailAddress: document.getElementById("emailAddressInput").value,
                textSize: document.getElementById("textSizeSelect").value
            },
            function () {
                alert("Saved!");
            }
        );

    },

    /*
    Render function.
     */
    render: function () {
        return (
            <div>
                <h3>Account Settings</h3>

                <div className="row">
                    Email address:<br />
                    <div className="row">
                        <input id="emailAddressInput" type="text" name="emailaddress" disabled/>
                        <input type="submit" value="Edit email" onClick={this.changeEmailInputState}/>
                    </div>

                    Text size:<br />
                    <select id="textSizeSelect">
                        <option value="10">10</option>
                        <option value="12">12</option>
                        <option value="14">14</option>
                        <option value="16">16</option>
                    </select><br />

                    Language:<br />
                    <select id="languageSelect">
                        <option value="Greek">Greek</option>
                        <option value="English">English</option>
                        <option value="Swahili">Swedish</option>
                    </select><br />
                </div>
                <br />

                <div className="row">
                    <div className="two columns u-pull-left">
                        <button onClick={this.sendPOST}>Save</button>
                    </div>

                    <div className="two columns u-pull-right">
                        <button className="md-close">Close</button>
                    </div>
                </div>
            </div>
        );
    }
});
