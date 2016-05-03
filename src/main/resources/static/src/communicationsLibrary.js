function getJson(URL, callback) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() { 
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(JSON.parse(JSON.stringify(xmlHttp.responseText)));
    }
    xmlHttp.open("GET", URL, true);
    xmlHttp.send();
}
