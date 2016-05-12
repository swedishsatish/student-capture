package studentcapture.login;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.validation.constraints.AssertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;

public class LoginDBControllerTest extends StudentCaptureApplicationTests {
	
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;
    
    //CSRF token generator, to be able to send http post
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    
    @Test
    public void registerUser() throws Exception {
        
    }
    
    @Test
    public void userAlreadyExistsInDB() throws Exception {
    	
    }    
    
    @Test
    public void incorrectEmail() throws Exception {
    	String email = "somethingemail.com";
    	LoginDBController c = new LoginDBController();
    	if(c.checkEmailFormat(email)) {
    		fail("Email is incorrect but passes");
    	}
    }
    
    @Test
    public void correctEmail() throws Exception {
    	String email = "something@email.com";
    	LoginDBController c = new LoginDBController();
    	if(!c.checkEmailFormat(email)) {
    		fail("Email is correct but fails");
    	}
    }
    
    @Test
    public void incorrectPasswordFormat() throws Exception {
    	String password = "korrekt123";
    	LoginDBController c = new LoginDBController();
    	if(c.checkPasswordFormat(password)) {
    		fail("Password is incorrect but passes");
    	}
    }
    
    @Test
    public void correctPasswordFormat() throws Exception {
    	String password = "Korrekt123";
    	LoginDBController c = new LoginDBController();
    	if(!c.checkPasswordFormat(password)) {
    		fail("Password is correct but fails");
    	}
    }
    
    @Test
    public void passwordEquals() throws Exception {
    	String password1 = "someThing";
    	String password2 = "someThing";
    	LoginDBController c = new LoginDBController();
    	if(!c.checkPasswordMatches(password1, password2)) {
    		fail("Password is not equal");
    	}
    }
    
    @Test
    public void passwordNotEquals() throws Exception {
    	String password1 = "sometHing";
    	String password2 = "someThing";
    	LoginDBController c = new LoginDBController();
    	if(c.checkPasswordMatches(password1, password2)) {
    		fail("Password equals");
    	}
    }
    
    @Test
    public void userLengthIncorrect() throws Exception {
    	String username = "1234";
    	LoginDBController c = new LoginDBController();
    	if(c.checkUsernameLength(username)) {
    		fail("Username is too short but passes");
    	}
    }
    
    @Test
    public void userLengthCorrect() throws Exception {
    	String username = "12344567890+";
    	LoginDBController c = new LoginDBController();
    	if(!c.checkUsernameLength(username)) {
    		fail("Username is correct but fails");
    	}
    }
    
    @Test
    public void testSalt() throws Exception {
    	LoginDBController c = new LoginDBController();
    	c.encryptPassword("ASDF");
    }
}
