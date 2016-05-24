package studentcapture.login;

import java.net.URISyntaxException;
import java.sql.Types;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.user.User;
import studentcapture.user.UserResource;
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
    
    @Autowired
    private JdbcTemplate jdbcMock;
    
    //CSRF token generator, to be able to send http post
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    
    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        mockMvc.perform(post("/register")
    			.param("firstname", "testfname")
    			.param("lastname", "testlname")
                .param("email", "testmail@mail.com")
                .param("username", "testuser")
                .param("password", "testpass")
                .param("confirmpassword", "testpass"));
        
    }
    
    @Test
    public void registerUser() throws Exception {
    	mockMvc.perform(post("/register")
    			.param("firstname", "testfname")
    			.param("lastname", "testlname")
                .param("email", "testmail1@mail.com")
                .param("username", "testuser1")
                .param("password", "testpass1")
                .param("confirmpassword", "testpass1"))
                .andExpect(status().isFound());
    }
    
    @Test
    public void getLoginPage() throws Exception{
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
    
    
    @Test
    public void getUserPageWhenNotLoggedIn() throws Exception{
        mockMvc.perform(get("/index"))
                .andExpect(status().isFound()); //Expect redirection to login page (status <302>)
    }
    
    
    @Test
    @WithMockUser(roles="USER")
    public void checkUserRights() throws Exception{
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk());
    }
    
   
    /**
     * This test expects that a user Lol123 exists, if it does not then this test will fail!
     * @throws Exception
     */
    @Test
    public void loginAsUser() throws Exception{
        mockMvc.perform(post("/login")
                .param("username", "testuser")
                .param("password", "testpass"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/index"));
    }
    
    
    @Test
    public void loginWrongDetails() throws Exception{
        
        //Wrong username
        mockMvc.perform(post("/login")
                .param("username", "error")
                .param("password", "test"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error=loginerror"));
        
        //Wrong password
        mockMvc.perform(post("/login")
                .param("username", "test")
                .param("password", "error"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error=loginerror"));
    }
    
    
    @Test
    public void logout() throws Exception{
        mockMvc.perform(post("/logout"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?logout"));
    }
    

    /**
     * Remove all content from the database
     */
    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Users;";
        //Reset serialize userid
        String sql2 = "ALTER TABLE users ALTER COLUMN userid RESTART WITH 1";
        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
    }
}