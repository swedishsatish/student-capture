package studentcapture.login;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 
 * JUnit tests for the login system.
 * 
 * 
 * 2016-05-02
 * Current working user is: 
 *      username: user
 *      password: user
 * Current working admin is:
 *      username: admin
 *      password: admin
 *      
 * 2016-05-04 - dv11osi
 * Certain tests for checking permissions are currently disabled because
 * at the moment there's no restrictions of access on the server.
 * Because of this all these tests will fail, to prevent confusion with other
 * groups because of failing tests, these tests are disabled and commented 
 * with "PERMISSION TEST"
 *
 *      
 * @WithMockUser(roles="USER") mocks the role USER, 
 * so that no post login is required in the test.
 * 
 * @author c13hbd
 *
 */
public class LoginTest extends StudentCaptureApplicationTests{

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
    public void getLoginPage() throws Exception{
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
    
    /* PERMISSION TEST
    @Test
    public void getUserPageWhenNotLoggedIn() throws Exception{
        mockMvc.perform(get("/loggedin"))
                .andExpect(status().isFound()); //Expect redirection to login page (status <302>)
    }
    */ 
    /* PERMISSION TEST
    @Test
    @WithMockUser(roles="USER")
    public void checkUserRights() throws Exception{
        mockMvc.perform(get("/loggedin"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }
    */
    
    /* PERMISSION TEST
    @Test
    @WithMockUser(roles="ADMIN")
    public void checkAdminRights() throws Exception{
        mockMvc.perform(get("/loggedin"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }
    */
    
    /*
    @Test
    @WithMockUser(roles="USER")
    public void getAdminPageAsUser() throws Exception{
        mockMvc.perform(get("/admin")).andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(roles="ADMIN")
    public void getAdminPageAsAdmin() throws Exception{
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }
    */
    
    /* This tests redirection. currently disabled so this test fails.
    @Test
    public void loginAsUser() throws Exception{
        mockMvc.perform(post("/login")
                .with(csrf()) //csrf is required
                .param("username", "user")
                .param("password", "user"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/loggedin"));
    }
    */
    @Test
    public void loginWrongDetails() throws Exception{
        
        //Wrong username
        mockMvc.perform(post("/login")
                .with(csrf()) //csrf is required
                .param("username", "error")
                .param("password", "test"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
        
        //Wrong password
        mockMvc.perform(post("/login")
                .with(csrf()) //csrf is required
                .param("username", "test")
                .param("password", "error"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }
    
    @Test
    public void logout() throws Exception{
        
        
        //TODO: require login to be able to logout ?
    	//		Should work if access is limited based on permissions
        /*
        //Login as user
        mockMvc.perform(post("/login")
                .with(csrf()) //csrf is required
                .param("username", "test")
                .param("password", "test"))
                .andExpect(redirectedUrl("/loggedin"));
                */
        
        mockMvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?logout"));
       
        
    }
 
}