var LoginForm = React.createClass({

    handleClickReg: function(){
        ReactDOM.render(<RegisterForm />, document.getElementById('loginPage'))
    },
    
    handleClickLogin: function(){       
        
        if(loginForm.Uname.value == "user" &&
           loginForm.Fpass.value == "password"){
            
            alert("Login SUCCESSFUL! but stay here for a while")
        }
        else
        {
            alert("Wrong username or password");
        }
    },

    render: function(){
        return (

            <div id="loginForm" onSubmit={this.handleClickLogin}>
             
                <h1>Student Capture</h1>

                        <input type="text" 
                               placeholder="Username" id="Uname" name="username"/>
                    
                        <input type="password" 
                               placeholder="Password" id="Fpass" name="password"/>

                <button type="submit">
                    Login
                </button>
                <button type="button" onClick={this.handleClickReg}>
                    Register
                </button>
                
            </div>
        );
    }
});

var RegisterForm = React.createClass({

    validateEmail: function() {
        var input = this.refs.EmailInput.value;
        var atpos = input.indexOf("@");
        var dotpos = input.lastIndexOf(".");
        if (atpos<1 || dotpos<atpos+2 || dotpos+2>=input.length) {
            return false;
        }
        return true;
    },
    
    setInvalid: function(){
        
    },

    validateAllFields: function(){
    
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

        if (!this.validateEmail())
        {
            alert('Please enter a valid email address on the format:\nexample@mail.domain');
            return;
        }
        
        if(this.refs.Fpass.value != this.refs.Spass.value){
            alert('Passwords did not match');
            return;
        }
	
		if(!this.refs.Fpass.checkValidity() || !this.refs.Spass.checkValidity())
    		alert('invalid password');
////ajax send
//        ReactDOM.render(<LoginForm />, document.getElementById('loginPage'));
		alert('accepted');

    },
    
    handleClickCancel: function(){

       ReactDOM.render(<LoginForm />, document.getElementById('loginPage'))
    },
    
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
    					 one lower case character and one numeric character.</p>
    					 
                         
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

ReactDOM.render(
    <LoginForm />,
    document.getElementById('loginPage')
);

//window.RegisterForm = RegisterForm;
