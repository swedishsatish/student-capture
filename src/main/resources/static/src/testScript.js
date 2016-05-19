function btn() {
var test;
	$.ajax({
            type: "GET",
            url: "/sessiontest",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(data){test = data;},
            failure: function(errMsg) {
                alert(errMsg);
            }
        });
   	alert("????" + test);
}

function submitfunc() {
 var test;
	$.ajax({
            type: "POST",
            url: "/sessiontest",
            dataType: "application/json",
			data: {coolString: document.getElementById("cString").value},
            success: function(data){test = data;},
            failure: function(errMsg) {
                alert(errMsg);
            }
        });
   	alert("????" + test);
}
