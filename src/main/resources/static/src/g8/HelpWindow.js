/**
 *
 *@author: Benjamin Björklund <c13bbd>,
 *         Tobias Estefors <dv13tes>
 *
 *@note: This react-class is rendered from Profile.js
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

window.HelpWindow = React.createClass({




    faqClickHandler: function () {
        ReactDOM.render(<FAQInfo />, document.getElementById('modal-container'));
        reloadScripts();
    },
    emailClickHandler: function () {
        ReactDOM.render(<EmailSupportWindow />, document.getElementById('modal-container'));
        reloadScripts();
    },
    render: function () {
        return (
            <div>
                <div id="helpContainer">
                    <h3>Have a question or need some help?</h3>

                    <h3>Read the</h3>
                    <button onClick={this.faqClickHandler}>FAQ</button>

                    <h3>or send and</h3>
                    <button onClick={this.emailClickHandler}>E-mail</button>
                </div>
                <div className="two columns u-pull-right">
                    <button className="md-close">Close</button>
                </div>
            </div>
        );
    }

});
