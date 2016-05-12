package studentcapture.login;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
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
    public void getDatabaseLoginPage() throws Exception{
        mockMvc.perform(get("/DB/login"))
                .andExpect(status().isBadRequest()); //Expect error status 400
    }
    
    @Test
    public void getDatabaseRegisterPage() throws Exception{
        mockMvc.perform(get("/DB/register"))
                .andExpect(status().isBadRequest()); //Expect error status 400
    }
    
    @Test
    public void createAndInsertUserTest() throws Exception {
        
    }
    
    @Test
    public void getCorrectUser() throws Exception {
    	
    }
    
    @Test
    public void userAlreadyExistsInDB() throws Exception {
    	
    }
}
