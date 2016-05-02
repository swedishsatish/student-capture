/**
 * Created by c13lbm on 4/27/16.
 */
// tutorial1.js
// PostBlob method uses XHR2 and FormData to submit
// recorded blob to the PHP server















var HardwareTest = React.createClass({

    componentDidMount: function(){
       /* var button = document.createElement("button");
        button.setAttribute("class","md-trigger");
        button.setAttribute("data-modal","modal-16");
        button.setAttribute("id","openModal");
        document.body.appendChild(button);


        var script1 = document.createElement("script");
        script1.setAttribute("src","js/classie.js");
        document.body.appendChild(script1);
        var script2 = document.createElement("script");
        script2.setAttribute("src","js/modalEffects.js");
        document.body.appendChild(script2);


        //document.getElementById("openModal").click();
        console.log($("#openModal"));
        //console.log(button);
        $("#openModal").trigger("click");*/
        
    },
    handleClick: function (event) {
        //var video = new FormData();
        //video.append("file.webm",document.getElementById("vidTest").src)
        //var video = document.getElementById("vidTest").src;
        console.log("sednd");
       /* $.get("https://teselecta:8443/video/posttest", {videoTest: video}, function (res) {
            console.log(res);
            var resVid = document.getElementById("vidRes");
            resVid.setAttribute("src",res);
            resVid.setAttribute("autoplay","true");
        });

*/
    },
    render: function() {

        return (
            <div>
                <h3>Hardware testing</h3>
                <div className="row" id="">
                    <div className="six columns">
                        <Recorder />

                    </div>
                    <div id="tst" className="six columns ">
                        
                    </div>
                </div>
                <div className="row">
                    <p id="internet-speed"></p>
                </div>
                <div className="row">
                    <div className="four columns u-pull-left">
                        <button id="record" className="recControls">Record</button>

                        <button id="stop" className="recControls" sdisabled>Stop</button>
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