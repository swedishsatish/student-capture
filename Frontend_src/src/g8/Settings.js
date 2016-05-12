/**
 * Created by ben on 2016-05-12.
 */

window.Settings = React.createClass({
    sendPOST: function () {

        $.post(
            "https://172.23.140.97:8443/settings",
            {
                userID: "ben the butcher",
                language: "swahili",
                emailAddress: "none@nowhere.no",
                textSize: "21",
                newUser: false
            },
            function(){
                alert("Wow!");
            }
        );

    },
    render: function () {
        return (
            <div>
                <p>Hejsan</p>
                <button onClick={this.sendPOST}>Clickl</button>
            </div>
        );
    }
});
