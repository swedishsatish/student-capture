/**
 * Made by Andreas Savva and Benjamin Björklund, Squad 8
 *
 * This is a settings menu for the user interface.
 */

window.Settings = React.createClass({

    /*
     Function that is automatically called after the component is rendered.
     It fills the settings panel with the users current settings.
     */
    componentDidMount: function () {
        var settingsArray = this.GETSettings();
        document.getElementById("emailAddressInput").value = settingsArray.emailAddress;
        document.getElementById("ts" + settingsArray.textSize).selected = true;
        document.getElementById("lang" + settingsArray.language).selected = true;
        document.getElementById("emailCheckbox").checked = settingsArray.receiveEmails;
    },

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
    PUTSettings: function () {

        $.ajax({
            url: "users/" + this.props.userID + "/settings",
            type: "PUT",
            async: false,
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                "language": document.getElementById("languageSelect").value + "",
                "email": document.getElementById("emailAddressInput").value + "",
                "mailUpdate": document.getElementById("emailCheckbox").checked,
                "textSize": document.getElementById("textSizeSelect").value + ""
            }),
            success: function (data, status) {
                console.log("saved");
            }.bind(this),
            error: function (xhr, status, err) {
                console.log("Settings.js: Error: PUTSettings");
            }.bind(this)
        });
    },

    /*
     Gets the saved settings for the current user. This is in order to show what the
     current settings are, for the user.
     */
    GETSettings: function () {

        // Array to fill with actual values, from the database, and return
        var settingsArray = {
            userID: "",
            language: "",
            emailAddress: "",
            textSize: "",
            receiveEmails: true
        };

        $.ajax({
            url: "users/" + this.props.userID + "/settings",
            type: "GET",
            async: false,
            success: function (data, status) {
                settingsArray.emailAddress = data.email;
                settingsArray.textSize = data.textSize;
                settingsArray.language = data.language;
                settingsArray.receiveEmails = data.mailUpdate;
            }.bind(this),
            error: function (xhr, status, err) {
                console.log("Settings.js: Error: GETSettings");
            }.bind(this)
        });
        return settingsArray
    },

    /*
     Render function.
     */
    render: function () {

        return (
            <div>
                <h3>Account Settings</h3>
                ​
                <div className="row">
                    Email address:<br />
                    <div className="row">
                        <input id="emailAddressInput" type="text" name="emailaddress" disabled/>
                        <input type="submit" value="Edit email" onClick={this.changeEmailInputState}/>
                    </div>
                    I want to receive email notifications:<br />
                    <input type="checkbox" id="emailCheckbox"/><br />
                    ​
                    Text size:<br />
                    <select id="textSizeSelect" selected="">
                        <option value="10" id="ts10">10</option>
                        <option value="12" id="ts12">12</option>
                        <option value="14" id="ts14">14</option>
                        <option value="16" id="ts16">16</option>
                    </select><br />
                    ​
                    Language:<br />
                    <select id="languageSelect">
                        <option value="Greek" id="langGreek">Greek</option>
                        <option value="English" id="langEnglish">English</option>
                        <option value="Swedish" id="langSwedish">Swedish</option>
                    </select><br />
                </div>
                <br />
                ​
                <div className="row">
                    <div className="two columns u-pull-left">
                        <button onClick={this.PUTSettings}>Save</button>
                    </div>
                    ​
                    <div className="two columns u-pull-right">
                        <button className="md-close">Close</button>
                    </div>
                </div>
            </div>
        );

    }
});