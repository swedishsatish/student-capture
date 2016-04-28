/**
 * File:	upload.js
 * Author:	Isak Hjelt
 * cs-user:	dv14iht
 * Date:	4/28/16
 *
 * Uploads a blob to the server.
 */

/**
 * POST a video to the server.
 *
 * @param theStream - the media stream to record.
 * @param {string} userID
 * @param {string} courseID
 * @param {string} examID
 */
function postToServer(blob,userID,courseID,examID) {
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function() {
        if (xhr.readyState == XMLHttpRequest.DONE) {
            var dataPOST = new FormData();

            console.log(xhr.responseText);

            var urlPOST = "https://localhost:8443" + xhr.responseText;
            var methodPOST = "POST";

            dataPOST.append("video", blob);
            dataPOST.append("videoName", "video.webm");
            dataPOST.append("userID", "user");

            var xhrPOST = new XMLHttpRequest();

            if ("withCredentials" in xhr) { // Chrome, Firefox, Opera
                xhrPOST.open(methodPOST, urlPOST, true);
                xhrPOST.send(dataPOST);

            } else if (typeof XDomainRequest != "undefined") { // IE
                xhrPOST = new XDomainRequest();
                xhrPOST.open(methodPOST, urlPOST);
                xhrPOST.send(dataPOST);
            } else { //None
                xhrPOST = null;
            }
        }
    }
        var url = "https://localhost:8443/video/inrequest?userID=" + userID +"&courseID=" + courseID+ "&examID=" + examID;
        var method = "GET";

        xhr.open(method, url, true);
        xhr.send();
}