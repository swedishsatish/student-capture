/**
 * File:    upload.js
 * Author:  Isak Hjelt
 * cs-user: dv14iht
 * Date:    4/28/16
 *
 * Uploads a blob to the server.
 */

"use strict";

/**
 * POST a video to the server.
 *
 * @param theStream - the media stream to record.
 * @param {string} userID
 * @param {string} courseID
 * @param {string} examID
 */
function postToServer(blob, userID, courseID, assignmentID) {
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            var dataPOST = new FormData();

            console.log(xhr.responseText);

            var urlPOST = "https://localhost:8443" + xhr.responseText;
            var methodPOST = "POST";

            dataPOST.append("video", blob);
            dataPOST.append("videoType", "submission");
            dataPOST.append("userID", userID);
            dataPOST.append("assignmentID", assignmentID);
            dataPOST.append("courseID", courseID);
            dataPOST.append("courseCode", courseID);

            var xhrPOST = new XMLHttpRequest();

            if ("withCredentials" in xhr) { // Chrome, Firefox, Opera
                xhrPOST.open(methodPOST, urlPOST, true);
                xhrPOST.send(dataPOST);

            } else if (typeof XDomainRequest !== "undefined") { // IE
                xhrPOST = new XDomainRequest();
                xhrPOST.open(methodPOST, urlPOST);
                xhrPOST.send(dataPOST);
            } else { //None
                xhrPOST = null;
            }
        }
    };
    var url = "https://localhost:8443/video/inrequest?userID=" + userID + "&courseID=" + courseID +
            "&assignmentID=" + assignmentID;
    var method = "GET";

    xhr.open(method, url, true);
    xhr.send();
}