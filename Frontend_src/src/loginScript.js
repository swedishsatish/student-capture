//Created by:
//Simon Lundmark

//Last update:
//3/8-16


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
            
           window.location = "index.html";
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
            <div id="loginForm">
                <h1>Student Capture</h1>
                <input type="text" 
                    placeholder="Username" ref="Uname" name="username"/>
            
                <input type="password" 
                    placeholder="Password" ref="Fpass" name="password"/>

                <button type="button" onClick={this.handleClickLogin}>
                    Login
                </button>
                
                <button type="button" onClick={this.handleClickReg}>
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
	
		if(!this.refs.Fpass.checkValidity() || !this.refs.Spass.checkValidity())
    		alert('invalid password');
    		
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
            <div id="registrationForm">
                
                <h1>Registration page</h1>
                
                        <input type="text" 
                               placeholder="First name" ref="Fname" required/>
                    
                        <input type="text" 
                               placeholder="Last name" ref="Lname" required/>

                        <input type="email" 
                               placeholder="example@mail.com" ref="EmailInput" required />
         
                        <input className="u-full-width" type="text" 
                               placeholder="Username" ref="Uname" required/>
    					  
                        <input type="password" 
                               placeholder="Password" ref="Fpass" 
                               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,}$" 
                               required/>

                        <input type="password" 
                               placeholder="Repeat password" ref="Spass" 
                               pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{6,}$" 
                               required />
                               
                               <p>Password must contain atleast one upper case character,
    					 one lower case character and one numeric character. 6 characters minimum.</p>
    					 
                         
                <button type="button" onClick={this.validateAllFields}>
                    Register
                </button>
                <button type="button" onClick={this.handleClickCancel}>
                    Cancel
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
