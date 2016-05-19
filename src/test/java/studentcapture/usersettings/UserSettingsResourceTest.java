package studentcapture.usersettings;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.model.Settings;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by andreassavva on 2016-05-12.
 *
 */
public class UserSettingsResourceTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate templateMock;

    private MockMvc mockMvc;
    private String requestMapping;
    private Settings settings;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(
                webApplicationContext).build();

        /* settings resource mapping = /users/{id}/settings */
        int id = 1;
        requestMapping = getNewRequestMapping(String.valueOf(id));

        /* Create a settings object and fill with test data. */
        settings = new Settings();
        settings.setEmail("c13swn@cs.umu.se");
        settings.setLanguage("english");
        settings.setMailUpdate(Boolean.TRUE);
        settings.setTextSize(12);
    }

    private String getNewRequestMapping(String id) {
        return "/users/" + id + "/settings";
    }

    /**
     * Left for further development.
     *
     * Not sure what functionality that already exists thanks to
     * tomcat and so on. Might be a good idea to handle the rest
     * ( @RequestMapping(method = { RequestMapping.a, RM.b, RM.c} )
     * with some informative http status code.
     *
     * get          allowed request.
     * post         allowed request.
     * put          allowed request.
     * delete       allowed request.
     * head         wrong request.
     * trace        wrong request.
     * options      work automatically?
     * connect      wrong request.
     * patch        wrong request.
     * @throws Exception
     */ /*
    @Test
    public void shouldRespondWithCorrectErrorOnWrongRequest() throws Exception {
        mockMvc.perform(head(requestMapping)).andExpect(status().isMethodNotAllowed());
    } */


    @Test
    public void shouldRespondWith422OnWrongId() throws Exception {
        String invalidMapping = getNewRequestMapping(String.valueOf(0));

        // get, post, put, delete
        mockMvc.perform(get(invalidMapping)).andExpect(status().isUnprocessableEntity());
        mockMvc.perform(post(invalidMapping)).andExpect(status().isUnprocessableEntity());
        //mockMvc.perform(put(invalidMapping)).andExpect(status().isUnprocessableEntity());
        //mockMvc.perform(delete(invalidMapping)).andExpect(status().isUnprocessableEntity());
    }


    /*
    @Test
    public void shouldX() throws Exception {

        //ResponseEntity<Settings> response = new ResponseEntity<>(settings, HttpStatus.OK);


        when(templateMock.postForEntity
                        (any(String.class),
                                any(LinkedMultiValueMap.class),
                                eq(String.class)))
                .thenReturn(response);

        mockMvc.perform(post("/settings")
                .param("userID", "user")
                .param("language", "english")
                .param("emailAddress", "c13swn@cs.umu.se")
                .param("textSize", "12")
                .param("newUser", "true"))
                .andExpect(status().isOk());
    }
    */
}
