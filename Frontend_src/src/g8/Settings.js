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

    componentDidMount: function () {
        var settingsArray = this.GETSettings();

        settingsArray.userID = "1";
        settingsArray.emailAddress = "benjamin@calleinc.se";
        settingsArray.language = "Swedish";
        settingsArray.textSize = "16";
        
        document.getElementById("emailAddressInput").value = settingsArray.emailAddress;
        document.getElementById("ts" + settingsArray.textSize).selected = true;
        document.getElementById("lang" + settingsArray.language).selected = true;

        //TODO: something
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
    POSTSettings: function () {
        $.post(
            window.globalURL + "/settings",
            {
                "userID": this.props.userID + "",
                "language": document.getElementById("languageSelect").value + "",
                "emailAddress": document.getElementById("emailAddressInput").value + "",
                "textSize": document.getElementById("textSizeSelect").value + ""
            },
            function () {
                console.log("Saved");
            }
        );

    },

    GETSettings: function () {

        var settingsArray = {
            userID: "",
            language: "",
            emailAddress: "",
            textSize: ""
        };
        console.log("GETSettings: userID: " + this.props.userID);
        $.ajax({
            url: window.globalURL + "/settings",
            type: "GET",
            async: false,
            data: {
                "userID": this.props.userID + ""
            },
            success: function (data, status) {
                console.log("Success GET");
            }.bind(this),
            error: function (xhr, status, err) {
                console.log("Error GET");
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

                <div className="row">
                    Email address:<br />
                    <div className="row">
                        <input id="emailAddressInput" type="text" name="emailaddress" disabled/>
                        <input type="submit" value="Edit email" onClick={this.changeEmailInputState}/>
                    </div>

                    Text size:<br />
                    <select id="textSizeSelect" selected="">
                        <option value="10" id="ts10">10</option>
                        <option value="12" id="ts12">12</option>
                        <option value="14" id="ts14">14</option>
                        <option value="16" id="ts16">16</option>
                    </select><br />

                    Language:<br />
                    <select id="languageSelect">
                        <option value="Greek"   id="langGreek">Greek</option>
                        <option value="English" id="langEnglish">English</option>
                        <option value="Swedish" id="langSwedish">Swedish</option>
                    </select><br />
                </div>
                <br />

                <div className="row">
                    <div className="two columns u-pull-left">
                        <button onClick={this.POSTSettings}>Save</button>
                    </div>

                    <div className="two columns u-pull-right">
                        <button className="md-close">Close</button>
                    </div>
                </div>
            </div>
        );

        
    }
});
