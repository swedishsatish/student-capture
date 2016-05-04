var NewProfile = React.createClass({
	render : function() {
		return <div className="three columns offset-by-nine" id="profile">
			<h6 id="profileName">{profileData.name}</h6>
		</div>
	}
});

var profileData = {name:'React Reactson'};

ReactDOM.render(<NewProfile />, document.getElementById('desktopHeader'));