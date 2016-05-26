/**
 *
 *@author: Benjamin Björklund <c13bbd>,
 *         Tobias Estefors <dv13tes>
 *
 *@note: This react-class is rendered from Profile.js
 **/



/**
 * Window for when the user presses 'Help'
 */
window.HelpWindow = React.createClass({

    /**
     * Handler for when the user presses FAQ
     */
    faqClickHandler: function () {
        ReactDOM.render(<FAQInfo />, document.getElementById('modal-container'));
        window.reloadScripts();
    },

    /**
     * Handler for when the user presses E-Mail
     */
    emailClickHandler: function () {
        ReactDOM.render(<EmailSupportWindow />, document.getElementById('modal-container'));
        reloadScripts();
    },
    
    render: function () {
        return (
            <div>
                <div className="row">
                    <div id="helpContainer">
                        <h3>Have a question or need some help?</h3>

                        <h3>Read the</h3>
                        <button onClick={this.faqClickHandler}>FAQ</button>

                        <h3>or send an</h3>
                        <button onClick={this.emailClickHandler}>E-mail</button>
                    </div>
                </div>
                <div className="row">
                    <div className="two columns u-pull-right">
                        <button className="md-close">Close</button>
                    </div>
                </div>
            </div>
        );
    }

});
