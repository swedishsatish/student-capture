
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
	console.log(2);
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
	render : function() {
		/*return <div className="three columns offset-by-nine" id="profile">
			<h6 id="profileName" onClick={this.clickHandler} className="md-trigger md-setperspective" data-modal="modal-19">{profileData.name}</h6>
			
		</div>*/
		return (
			<div className="three columns offset-by-nine dropdown" id="profile">
				<h6 id="profileName" className="dropdown-head">{this.props.name}</h6>
				<div className="dropdown-content">
					<h6 onClick={this.clickHandler} className="md-trigger md-setperspective dropdown-head" data-modal="modal-19">Test equipment</h6>
				</div>

			</div>
		);
	}
});



//ReactDOM.render(<NewProfile />, document.getElementById('desktopHeader'));