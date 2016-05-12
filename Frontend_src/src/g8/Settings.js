/**
 * Created by ben on 2016-05-12.
 */

window.Settings = React.createClass({

    textSize: 12,
    emailAddress: "",
    language: "",

    changeEmailInputState: function () {
        var emailInputStateDisabled = document.getElementById("emailAddressInput");

        if (emailInputStateDisabled.disabled) {
            emailInputStateDisabled.disabled = false;
        } else {
            emailInputStateDisabled.disabled = true;
        }
    },

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
