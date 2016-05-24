//Created by:
//Simon Lundmark, Erik Andersson

//Refactored by:
//Henrik Bylund

//Last update:
//2016-05-24

//----------------------------showing/hiding forms------------------------------
// Handles changing of which form is active

// Get the forms
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

// Animate change in visible form
// param toHide DOM element
// param toShow DOM element
function hideShow(toHide, toShow){
    fadeIn(toShow);
    fadeOut(toHide);
    toHide.reset();
}

//------------------------------------animation---------------------------------
// Handles the animation for changing between different forms

// Fade out
// param el DOM element
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

// Fade in
// param el DOM element
// param display Value for CSS property display on element el
function fadeIn(el, display){
  el.style.opacity = 0;

  //Default value if param display is not set
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
// Sets listeners for showing and hiding the modal

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
// Parses errors from the current URL and alerts the user which error occurred

// Get array of parameter value pairs from the current URL query string
var urlVars = window.location.search.toLowerCase().substr(1).split("&");
var err = false, type = "";
// For each pair
for(var i=0; i<urlVars.length; i++) {
    // Split into name and value, and check for error parameter
    var varSplit = urlVars[i].split("=");
    if(varSplit[0]==="error"){
        if(varSplit.length>1){
            for(var j=1; j<varSplit.length; j++) {
                type += varSplit[j];
            }
        }
        err = true;
        break;
    // Defined in RegistrationController.java
    } else if(varSplit[0]==="success") {
    	alert("Registration success!");
    	break;
    // Defined in ResetPasswordController.java
    } else if(varSplit[0]==="passwordemail") {
    	alert("Check your inbox for an email with the link to reset your"
    	        + "password!");
        break;
    // Defined in ResetPasswordController.java
    } else if(varSplit[0]==="passwordchanged") {
    	alert("Password changed!");
        break;
    }
    
}
/*


Defined in ErrorFlags.java
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
            msg = "Invalid email";
            break;
        case "emailexists":
            msg = "Email already associated with an account";
            break;
        case "userexists":
            msg = "Username already associated with an account";
            break;
        case "loginerror":
            msg = "Invalid username or password";
            break;
        case "badtoken":
        	msg = "Invalid or missing token";
        	break;
        case "emailusernamemismatch":
            msg = "Username and email do not belong to the same user."
            break;
        default:
            msg = "Unknown error";
            if(type !== ""){
                msg+= " of type " + type;
            }
            break;
    }
    alert("Error:\n"+msg);
}
