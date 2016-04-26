
var ReactTransitionGroup = React.addons.CSSTransitionGroup;

var LoginForm = React.createClass({

    getInitialState: function() {
    return { mounted: false };
  },
  componentDidMount: function() {
    this.setState({ mounted: true });
  },

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
        <ReactTransitionGroup transitionName="example" 
                              transitionEnterTimeout={500}
                              transitionLeaveTimeout={300}>

            <form id="loginForm" onSubmit={this.handleClickLogin}>
             <div class="row">
                <h1>Student Capture</h1>
                </div>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="text" 
                               placeholder="Username" id="Uname" />
                    </div>
                </div>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="password" 
                               placeholder="Password" id="Fpass" />
                    </div>
                </div>
                
                <button type="submit">
                    Login
                </button>
                <button type="button" onClick={this.handleClickReg}>
                    Register
                </button>
                
            </form>
       </ReactTransitionGroup>
        );
    }
});


var RegisterForm = React.createClass({

    handleClickRegister: function(){
        alert("Registration was very unsuccessful.... no server!!")
//        ReactDOM.render(<LoginForm />, document.getElementById('loginPage'))
    },
    
    handleClickCancel: function(){
        
        ReactDOM.render(<LoginForm />, document.getElementById('loginPage'))
    },
    

    render: function() {
        return (
            <form id="registrationForm" onSubmit={this.handleClickRegister}>
                
                <h1>Registration page</h1>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="text" 
                               placeholder="First name" id="Fname" />
                    </div>
                </div>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="text" 
                               placeholder="Last name" id="Lname" />
                    </div>
                </div>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="email" 
                               placeholder="example@mail.com" id="EmailInput" />
                    </div>
                </div>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="text" 
                               placeholder="Username" id="Uname" />
                    </div>
                </div>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="password" 
                               placeholder="Password" id="Fpass" />
                    </div>
                </div>
                <div class="row">
                    <div class="four columns">
                        <input class="u-full-width" type="password" 
                               placeholder="Repeat password" id="Spass" />
                    </div>
                </div>
 
                <button type="submit">
                    Register
                </button>
                <button type="button" onClick={this.handleClickCancel}>
                    Cancel
                </button>

            </form>
        );
    }
});


ReactDOM.render(
    <LoginForm />,
    document.getElementById('loginPage')
);

//window.RegisterForm = RegisterForm;
