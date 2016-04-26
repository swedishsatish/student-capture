package test;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringTestApplication.class)
@WebAppConfiguration
public class LoginTest {

    private MockMvc mockMvc;

    //@Autowired
    //ApplicationContext context;
    
    @Test
    public void getLoginPage() throws Exception{
        mockMvc.perform(get("/login")).andExpect(status().isOk());
    }
    
    @Test
    public void getRedirected() throws Exception {
    	mockMvc.perform(post("/login")).andExpect(status().isOk());
    }

}
