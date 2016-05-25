/**
 * Created by c13lbm on 4/27/16.
 */


var StudentVideoGallery = React.createClass({


    render: function () {

//        var submissionURL = "assignments/"+"1200"+"/submissions/";
        var submissionURL = "assignments/"+"1200"+"/submissions/"+"2/assignment.webm";

        console.log("ASSIGNMENT ID : " + this.props.assignment);
        console.log("SUTDNET ID : " + this.props.course);


        var xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function() {
                if (xhttp.readyState == 4 && xhttp.status == 200) {

                console.log(xhttp.responseText);

//                var submissionsJson = JSON.parse(xhttp.responseText);

  //              submissionsJson.forEach(function (hej, err) {

    //                console.log(hej.studentID);

      //          });






        //            ReactDOM.render(submissionsJson, document.getElementById('courseContent'));

                }
            };

        xhttp.open("GET", submissionURL, true);
        xhttp.send();



        return (

            <div>
                <p>helloess!!!!</p>
            </div>
        );
    }

});

window.StudentVideoGallery = StudentVideoGallery;

