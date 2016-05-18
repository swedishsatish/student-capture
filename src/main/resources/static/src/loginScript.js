//Created by:
//Simon Lundmark

//Last update:
//2016-11-05

var loginForm = document.getElementById('loginPage');
var regForm = document.getElementById('registerPage');
var lostForm = document.getElementById('lostPasswordPage');

// Get the button
var btn = document.getElementById("registerButton");
var cnclBtn = document.getElementById("cnclBtn");
var cnclBtn2 = document.getElementById("cnclBtn2");
var lostLink = document.getElementById("lostLink");

// When the user clicks the button
btn.onclick = function() {
    loginForm.reset();

    fadeIn(regForm);
    fadeOut(loginForm);
}

lostLink.onclick = function() {
    loginForm.reset();
    fadeOut(loginForm);
    fadeIn(lostForm);
}


// When the user clicks the button
cnclBtn.onclick = function() {
    fadeIn(loginForm);
    fadeOut(regForm);
    regForm.reset();
}

// When the user clicks the button
cnclBtn2.onclick = function() {
    fadeIn(loginForm);
    fadeOut(lostForm);
    lostForm.reset();
}
//-------------------------------------------------------------------------

// fade out

function fadeOut(el){
  el.style.opacity = 1;

  (function fade() {
    if ((el.style.opacity -= .1) < 0) {
      el.style.display = "none";
    } else {
      requestAnimationFrame(fade);
    }
  })();
}

// fade in

function fadeIn(el, display){
  el.style.opacity = 0;
  el.style.display = display || "block";

  (function fade() {
    var val = parseFloat(el.style.opacity);
    if (!((val += .1) > 1)) {
      el.style.opacity = val;
      requestAnimationFrame(fade);
    }
  })();
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
error=usernamelength
error=passwordformat
error=passwordmatch
error=emailformat
error=emailexists
error=userexists
*/
if(err){
    var msg;
    switch(type){
        case "usernamelength":
            msg = "Username is too short";
            break;
        case "passwordformat":
            msg = "Password is not in valid format";
            break;
        case "passwordmatch":
            msg = "The two passwords do not match";
            break;
        case "emailformat":
            msg = "Invalid Email";
            break;
        case "emailexists":
            msg = "Email already associated with an account";
            break;
        case "userexists":
            msg = "Username already used by another user";
            break;
        default:
            msg = "Unknown Error";
            break;
    }
    alert("TERRIBLE ERROR\n"+msg);
}
