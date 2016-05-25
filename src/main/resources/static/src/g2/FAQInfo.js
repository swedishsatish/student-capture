/**
 * FAQInfo displays the FAQ component.
 * Change information displayed in the FAQ by changing
 * the content inside the <h> and <p> tags.
 */

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

var FAQInfo = React.createClass({

    backClickHandle: function () {
        ReactDOM.render(<HelpWindow />, document.getElementById('modal-container'));
        reloadScripts();
    },
    render: function () {
        return (
            <div >
                <div id="FAQ-header">
                    <h1>FAQ</h1>
                </div>
                <div className="row" id="faq-className">
                    <div className="twelve columns" id="faq-text">
                    <h5>Vad är detta?</h5>
                      <p>Här ska en relevant fråga som en användare vill få svar på, besvaras. Detta
                         medför att ansvarige för systemet slipper svara på återkommande frågor.
                      </p>
                        <h5>Hur lägger man till frågor?</h5>
                        <p>Du går in i FAQinfo.js  i projektet student-capture och lägger till en ny.
                           Man kan då också ta bort denna  + den ovanstående och ersätta dessa med
                           mer relevant information
                        </p>
                        <h5>Hur svara man på frågan?</h5>
                            <p>Samma sak som ovanstående, men inom en paragraf istället...</p>
                        <h5>Question 3</h5>
                            <p>Hejsan</p>
                        <h5>Question 4</h5>
                            <p>Hejsan</p>
                        <h5>Question 5</h5>
                            <p>Hejsasn</p>
                        <h5>Question 6</h5>
                            <p>Hejsan</p>
                        <h5>Question 7</h5>
                            <p>Hejsan</p>
                        <h5>Question 8</h5>
                            <p>Hejsan</p>
                    </div>
                </div>
                <div className="row">
                    <div className="two columns u-pull-right">
                        <div className="md-close button primary-button SCButton">Close</div>
                        <div className="button primary-button SCButton" onClick={this.backClickHandle}>Back</div>
                    </div>
                </div>
            </div>
        );
    }
});

window.FAQInfo = FAQInfo;