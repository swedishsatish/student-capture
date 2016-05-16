//Created by:
//Simon Lundmark

//Last update:
//2016-11-05

var loginForm = document.getElementById('loginPage');
var regForm = document.getElementById('registerPage');

// Get the button
var btn = document.getElementById("registerButton");
var cnclBtn = document.getElementById("cnclBtn");

// When the user clicks the button
btn.onclick = function() {
    loginForm.reset();
    loginForm.style.display = "none";
    regForm.style.display = "block";
    
}

// When the user clicks the button
cnclBtn.onclick = function() {
    loginForm.style.display = "block";
    regForm.style.display = "none";
    regForm.reset();
}
//------------------------------------modal-------------------------------------

var modal = document.getElementById('myModal');

// Get the button that opens the modal
var btn = document.getElementById("modalBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal 
btn.onclick = function() {
    modal.style.display = "block";
}

// When the user clicks on (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}


//window.RegisterForm = RegisterForm;

//--------------------------------error handling--------------------------------
var urlVars = window.location.search.toLowerCase().substr(1).split("&");
var err = false, type = "";
for(var i=0; i<urlVars.length; i++) {
    var varSplit = urlVars[i].split("=");
    if(varSplit[0]==="error"){
        console.log(varSplit);
        if(varSplit.length>1){
            for(var j=1; j<varSplit.length; j++) {
                type += varSplit[j];
            }
        }
        err = true;
        break;
    }
}
/*
error=invalidEmail
error=invallidPassword
error=duplicateUsername
error=duplicateEmail
*/
if(err){
    var msg;
    switch(type){
        case "login":
            msg = "Login failed";
            break;
        case "register":
            msg = "Registration failed";
            break;
        case "invalidEmail":
            msg = "Invalid Email";
            break;
        case "invalidPassword":
            msg = "Invalid Email";
            break;
        case "duplicateUsername":
            msg = "Username already taken";
            break;
        case "duplicateEmail":
            msg = "Email already associated with an account";
            break;
        default:
            msg = "Unknown Error";
            break;
    }
    alert("TERRIBLE ERROR \n"+msg);
}
