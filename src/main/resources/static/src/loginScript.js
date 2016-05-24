//Created by:
//Simon Lundmark

//Last update:
//2016-19-05

//-----------------------showing/hiding forms-------------------------

//get the forms
var loginForm = document.getElementById('loginPage');
var regForm = document.getElementById('registerPage');
var lostForm = document.getElementById('lostPasswordPage');

// Get the buttons
var regBtn = document.getElementById("registerButton");
var cnclBtn = document.getElementById("cnclBtn");
var cnclBtn2 = document.getElementById("cnclBtn2");
var lostLink = document.getElementById("lostLink");

// When the user clicks the reg button
regBtn.onclick = function() {
    hideShow(loginForm, regForm);
}

// When the user clicks the lost password link
lostLink.onclick = function() {
    hideShow(loginForm, lostForm);
}


// When the user clicks the cancel button
cnclBtn.onclick = function() {
    hideShow(regForm, loginForm);
}

// When the user clicks the cancel button
cnclBtn2.onclick = function() {
    hideShow(lostForm, loginForm);
}

function hideShow(toHide, toShow){
    fadeIn(toShow);
    fadeOut(toHide);
    toHide.reset();
}

//------------------------------------animation-------------------------------------

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

//get the modal
var modal = document.getElementById('myModal');

// Get the button that opens the modal
var btn = document.getElementById("modalBtn");

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the modal 
btn.onclick = function() {
    fadeIn(modal);
}

// When the user clicks on (x), close the modal
span.onclick = function() {
    fadeOut(modal);
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
    if (event.target == modal) {
        fadeOut(modal);
    }
}

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
    } else if(varSplit[0]==="success") {
    	alert("Registration success!");
    } else if(varSplit[0]==="passwordemail") {
    	alert("Check your inbox for an email with the link to reset your password!");
    } else if(varSplit[0]==="passwordchanged") {
    	alert("Password changed!");
    } 
    
}
/*
error=usernamelength
error=passwordformat
error=passwordmatch
error=emailformat
error=emailexists
error=userexists
error=emailusernamemismatch
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
        case "loginerror":
            msg = "Invalid username or password";
            break;
        case "badtoken":
        	msg = "Invalid or missing token";
        	break;
        case "emailusernamemismatch":
            msg = "Username and Email do not belong to the same user."
            break;
        default:
            msg = "Unknown Error";
            break;
    }
    alert("Error:\n"+msg);
}
