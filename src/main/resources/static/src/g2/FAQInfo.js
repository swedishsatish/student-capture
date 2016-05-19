/**
 * FAQInfo displays the FAQ component.
 * Change information displayed in the FAQ by changing
 * the content inside the <h> and <p> tags.
 */
var FAQInfo = React.createClass({
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
                            <p>Hejsan</p>
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
                        <button className="md-close">Close</button>
                    </div>
                </div>
            </div>
        );
    }
});

window.FAQInfo = FAQInfo;