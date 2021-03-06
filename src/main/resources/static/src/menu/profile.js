/**
 * generate modal scripts
 */
function genScripts(){
	var script1 = document.createElement("script");
	script1.setAttribute("src","src/modals/classie.js");

	script1.setAttribute("id","script1");
	document.body.appendChild(script1);
	var script2 = document.createElement("script");
	script2.setAttribute("id","script2");
	script2.setAttribute("src","src/modals/modalEffects.js");

	document.body.appendChild(script2);
	
}

/**
 * remove and regenerate modal scripts.
 */
window.reloadScripts = function() {
	var script1 = document.getElementById("script1");
	var script2 = document.getElementById("script2");
	document.body.removeChild(script1);
	document.body.removeChild(script2);
	genScripts();
}


window.NewProfile = React.createClass({
	componentDidMount: function(){
		genScripts();
	},
	clickHandler: function () {
		ReactDOM.render(<HardwareTest />, document.getElementById('modal-container'));

		reloadScripts();
		var container = document.getElementById("tst");
		if(container.childNodes.length > 1){
			container.removeChild(container.childNodes.item(1));
		}
		$("#internet-speed").text("");
	},
    clickFaqHandler: function () {
		ReactDOM.render(<HelpWindow />, document.getElementById('modal-container'));
        reloadScripts();
    },

	clickSettingsHandler: function () {
		ReactDOM.render(<Settings userID={this.props.uid}/>, document.getElementById('modal-container'));
		reloadScripts();
	},
	clickLogoutHandler: function () {
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.onreadystatechange = function() {
            window.location.pathname = "/login";
        }
        xmlHttp.open("POST", window.location.protocol+"//"+window.location.host+"/logout", true); // true for asynchronous
        xmlHttp.send(null);

	},
	render : function() {
		/*return <div className="three columns offset-by-nine" id="profile">
			<h6 id="profileName" onClick={this.clickHandler} className="md-trigger md-setperspective" data-modal="modal-19">{profileData.name}</h6>
			
		</div>*/
		return (
			<div className="three columns offset-by-nine dropdown" id="profile">
				<h6 id="profileName" className="dropdown-head">{this.props.name}</h6>
				<div className="dropdown-content">
					<h6 onClick={this.clickHandler} className="md-trigger dropdown-head"
						data-modal="modal-16">Test equipment</h6>
					<h6 onClick={this.clickFaqHandler} className="md-trigger dropdown-head"
						data-modal="modal-16">Help</h6>
					<h6 onClick={this.clickSettingsHandler} className="md-trigger dropdown-head"
						data-modal="modal-16">Settings</h6>
					<h6 onClick={this.clickLogoutHandler} className="dropdown-head">Log Out</h6>
				</div>

			</div>
		);
	}
});



//ReactDOM.render(<NewProfile />, document.getElementById('desktopHeader'));