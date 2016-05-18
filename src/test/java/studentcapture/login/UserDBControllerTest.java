package studentcapture.login;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

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
import org.springframework.web.util.UriComponentsBuilder;

import studentcapture.config.StudentCaptureApplicationTests;

/**
 * Login controller tests
 * @author dv11osi
 *
 */
public class UserDBControllerTest extends StudentCaptureApplicationTests {
	
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;
    
    private UserDBController c;

    
    //CSRF token generator, to be able to send http post
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    
    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        
        c = new UserDBController();
    }
    
    @Test
    public void getDatabaseUsernamePage() throws Exception{
        mockMvc.perform(get("/DB/userNameExist"))
                .andExpect(status().isBadRequest()); //Expect error status 400
    }
    
    @Test
    public void getDatabasePasswordPage() throws Exception{
        mockMvc.perform(get("/DB/getHpswd"))
                .andExpect(status().isBadRequest()); //Expect error status 400
    }
    
    @Test
    public void getDatabaseRegisterPage() throws Exception{
        mockMvc.perform(get("/DB/addUser"))
                .andExpect(status().isBadRequest()); //Expect error status 400
    }
    
    @Test
    public void createAndInsertUserTest() throws Exception {
        
    }
    
    @Test
    public void getCorrectUser() throws Exception {
    	
    }    
    
    @Test
    public void incorrectEmail() throws Exception {
    	String email = "somethingemail.com";
    	if(c.checkEmailFormat(email)) {
    		fail("Email is incorrect but passes");
    	}
    }
    
    @Test
    public void correctEmail() throws Exception {
    	String email = "something@email.com";
    	if(!c.checkEmailFormat(email)) {
    		fail("Email is correct but fails");
    	}
    }
    
    @Test
    public void incorrectPasswordFormat() throws Exception {
    	String password = "korrekt123";
    	if(c.checkPasswordFormat(password)) {
    		fail("Password is incorrect but passes");
    	}
    }
    
    @Test
    public void correctPasswordFormat() throws Exception {
    	String password = "Korrekt123";
    	if(!c.checkPasswordFormat(password)) {
    		fail("Password is correct but fails");
    	}
    }

    @Test
    public void userLengthIncorrect() throws Exception {
    	String username = "1234";
    	if(c.checkUsernameLength(username)) {
    		fail("Username is too short but passes");
    	}
    }
    
    @Test
    public void userLengthCorrect() throws Exception {
    	String username = "12344567890+";
    	if(!c.checkUsernameLength(username)) {
    		fail("Username is correct but fails");
    	}
    }
    
    @Test
    public void testSalt() throws Exception {
    	c.encryptPassword("ASDF");
    }
    /*
    @Test
    public void testResetPassword() throws Exception{
        mockMvc.perform(post("/testResetPassword")
                //.with(csrf()) //csrf is required
                .param("email", "my_email"))
                .andExpect(status().isOk());
    }
    */
    /*
    @Test
    public void testLostPassword() throws Exception{
        mockMvc.perform(post("/lostPassword")
                //.with(csrf()) //csrf is required
                .param("email", "my_email")
                .param("username", "my_username"))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testEmailExist() throws Exception{
        mockMvc.perform(post("/DB/userEmailExist")
                //.with(csrf()) //csrf is required
                .param("email", "my_email"))
                //.param("username", "my_username"))
                .andExpect(status().isOk());
    }*/
}
