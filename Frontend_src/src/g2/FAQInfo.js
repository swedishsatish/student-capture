/**
 * Created by Jonas on 2016-05-12.
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
                    <h5>Question 1</h5>
                      <p>Hejsan</p>
                        <h5>Question 2</h5>
                        <p>Hejsan</p>
                        <h5>Question 1</h5>
                        <p>Hejsan</p>
                        <h5>Question 2</h5>
                        <p>Hejsan</p>
                        <h5>Question 1</h5>
                        <p>Hejsan</p>
                        <h5>Question 2</h5>
                        <p>Hejsan</p>
                        <h5>Question 1</h5>
                        <p>Hejsan</p>
                        <h5>Question 2</h5>
                        <p>Hejsan</p>
                        <h5>Question 1</h5>
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