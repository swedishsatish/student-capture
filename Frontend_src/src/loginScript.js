//Created by:
//Simon Lundmark

//revision by:
//Filip Golles

//Last update:
//2016-05-04


// Class for the login form.
var LoginForm = React.createClass({
    
    // Function that swaps the ligin form with the register form. 
    handleClickReg: function(){
        ReactDOM.render(<RegisterForm />, document.getElementById('loginPage'))
    },
    
    // Temporary login
    handleClickLogin: function(){       

        if(this.refs.Uname.value == "user" &&
           this.refs.Fpass.value == "password"){
            
           window.location = "demo.html";
        }
        else
        {
            alert("Wrong username or password");
        }
    },

    // Render function, defines what is to be shown for this class
    render: function(){
        return (
            // div containing all the inputs.
            <div className="forms">
                <img src="images/sc-logo.png" id="loginLogo" />
                <input type="text" className="loginField" placeholder="username" ref="Uname" name="username"/>
            
                <input type="password" className="loginField" placeholder="password" ref="Fpass" name="password"/>

                <button type="button" className="SCButton" id="loginButton" onClick={this.handleClickLogin}>
                    Login
                </button>
                
                <button type="button" className="SCButton" id="registerButton" onClick={this.handleClickReg}>
                    Register
                </button>
                
            </div>
        );
    }
});

// Class for the Register form.
var RegisterForm = React.createClass({
    
    // Function to check that all the fields are valid
    validateAllFields: function(){
        
        //if any is empty
        if(this.refs.Fname.value == ""
            || this.refs.Fname.value == ""
            || this.refs.Lname.value == ""
            || this.refs.EmailInput.value == ""
            || this.refs.Uname.value == ""
            || this.refs.Fpass.value == ""
            || this.refs.Spass.value == ""
        ){
            alert("Fill all fields");
            return;
        }
        
        // if email is wrong
        if (!this.refs.EmailInput.checkValidity())
        {
            alert('Please enter a valid email address on the format:\nexample@mail.domain');
            return;
        }
	
		if(!this.refs.Fpass.checkValidity() || !this.refs.Spass.checkValidity()){
    		alert('invalid password');
    		return;
    		}
    		
    		// if passwords are not the same
        if(this.refs.Fpass.value != this.refs.Spass.value){
            alert('Passwords did not match');
            return;
        }

        // send form (todo!)        
                
        //Tell the user it was successful 
		alert('accepted');
		ReactDOM.render(<LoginForm />, document.getElementById('loginPage'));

    },
    
    // Return to loginForm
    handleClickCancel: function(){

       ReactDOM.render(<LoginForm />, document.getElementById('loginPage'))
    },
    
    // Render function, defines what is to be shown for this class
    
    // Define password field, with pattern. that it should 
    // contain one big character, one small and one number 
    // atleast and 6 character minimum.
    render: function() {
        return (
            <div className="forms" id="regPage">
                
                <h2 id="regText">registration</h2>
                
                        <input type="text" id="firstName" className="loginField nameField" placeholder="first name" ref="Fname" required/>
                    
                        <input type="text" id="lastName" className="loginField nameField" placeholder="last name" ref="Lname" required/>

                        <input type="email" className="loginField" placeholder="example@mail.com" ref="EmailInput" required 
                        pattern="^\S+@(([a-zA-Z0-9]([a-zA-Z0-9\-]{0,61}[a-zA-Z0-9])?\.)+[a-zA-Z]{2,6})$" />
         
                        <input className="u-full-width" type="text" className="loginField" placeholder="username" ref="Uname" required/>
    					  
                        <input type="password" id="firstPass" className="loginField nameField"
                               placeholder="password" ref="Fpass" 
                               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,}$" 
                               required/>
                        
                        <p className="tooltipText">Password must contain atleast one upper case character,
    					 one lower case character and one numeric character.</p> 

                        <input type="password" id="secondPass" className="loginField nameField"
                               placeholder="repeat password" ref="Spass" 
                               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,}$" 
                               required />
                               
                               <p className="tooltipText">Password must contain atleast one upper case character,
    					 one lower case character and one numeric character.</p> 

                <button type="button" id="cnclBtn" className="SCButton loginField nameField" onClick={this.handleClickCancel}>
                    Cancel
                </button>
                <button type="button" id="regBtn" className="SCButton loginField nameField" onClick={this.validateAllFields}>
                    Register
                </button>
            </div>
        );
    }
});

//render login.
ReactDOM.render(
    <LoginForm />,
    document.getElementById('loginPage')
);

//window.RegisterForm = RegisterForm;
