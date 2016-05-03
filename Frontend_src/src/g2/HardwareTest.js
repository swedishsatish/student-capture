/**
 * Created by c13lbm on 4/27/16.
 */



var HardwareTest = React.createClass({


    render: function() {

        return (
            <div>
                <h3>Hardware testing</h3>
                <div className="row" id="">
                    <div className="six columns"><h5>Recording you</h5>
                        <Recorder />

                    </div>
                    <div id="tst" className="six columns "><h5>The recording from server</h5>
                        
                    </div>
                </div>
                <div className="row">
                    <p id="internet-speed"></p>
                </div>
                <div className="row">
                    <div className="four columns u-pull-left">
                        <button id="record" className="recControls">Record</button>

                        <button id="stop" className="recControls" disabled>Stop</button>
                    </div>
                    <div className="two columns u-pull-right">
                        <button className="md-close">Close</button>
                    </div>
                </div>
            </div>
        );
    }
});

window.HardwareTest = HardwareTest;