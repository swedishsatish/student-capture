/**
 * Created by c13lbm on 4/27/16.
 */
var playVideo = function (fName) {
    //CALLBACK FUNCTION, Download video.
    //openInNewTab('uploads/' + fName.replace(/"/g, ''));
    //window.open('uploads/' + fName.replace(/"/g, ''),'_blank');
    //TODO: play returned video-
    //console.log(fName);
    /* var vid = document.getElementById("returnTestVid");
     vid.src = "data:video/webm;base64,"+fName;
     vid.play();*/


    var container = document.getElementById("tst");
    if(container.childNodes.length > 1){
        container.removeChild(container.childNodes.item(1));
    }

    var mediaElement = document.createElement("video");

    var source = document.createElement('source');
    source.src = "data:video/webm;base64,"+fName;

    source.type = 'video/webm; codecs="vp8, vorbis"';

    mediaElement.setAttribute("width","100%");
    mediaElement.setAttribute("height","100%");
    mediaElement.appendChild(source);
    mediaElement.controls = true;
    container.appendChild(mediaElement)
    mediaElement.play();

}


var HardwareTest = React.createClass({

     playVideo: function (fName) {
        console.log("callbak")


        var container = document.getElementById("tst");
        if(container.childNodes.length > 1){
            container.removeChild(container.childNodes.item(1));
        }

        var mediaElement = document.createElement("video");

        var source = document.createElement('source');
        source.src = "data:video/webm;base64,"+fName;

        source.type = 'video/webm; codecs="vp8, vorbis"';

        mediaElement.setAttribute("width","100%");
        mediaElement.setAttribute("height","100%");
        mediaElement.appendChild(source);
        mediaElement.controls = true;
        container.appendChild(mediaElement)
        mediaElement.play();

    },
    calcSpeed: function (blobsize, sendTime) {

        $("#internet-speed").text(function () {
            var now = Date.now();
            var mbsec = (blobsize/((now - sendTime)/1000));
            //console.log("mbsec " + mbsec + "now = " + now/1000 + "stime" + sendTime/1000);
            return "Upload speed = "+ mbsec.toFixed(2) + "MB/s"
        });
    },
    render: function() {

        return (
            <div>
                <h3>Hardware testing</h3>
                <div className="row" id="">
                    <div className="six columns"><h5>Recording you</h5>
                        <Recorder playCallback={this.playVideo} autoRec="false" calc={this.calcSpeed} postURL="THISURL"/>

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