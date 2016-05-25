package studentcapture.usersettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.user.UserDAO;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO: Refactor UserDAO spy.
 *
 * Created by andreassavva on 2016-05-12.
 * @Author tfy12ajn
 */
public class UserSettingsResourceTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private String requestMapping;
    private String requestMappingInvalidID;
    /** original test string, as json "{"language":"english","email":"test@email.test","mailUpdate":true,"textSize":12}" */
    private static String json_test_string = "{\"language\":\"english\",\"email\":\"test@email.test\",\"mailUpdate\":true,\"textSize\":12}";

    @Before
    public void BeforeClass() {
        mockMvc = MockMvcBuilders.webAppContextSetup(
                webApplicationContext).build();

        /* /users/{id}/settings resource mapping. */
        requestMapping = getNewRequestMapping(String.valueOf(1));
        requestMappingInvalidID = getNewRequestMapping(String.valueOf(0));
    }

    @Before
    public void SetUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(
                webApplicationContext).build();
    }

    /**
     * RequestMapping with custom id.
     * @param id the id.
     * @return the mapping.
     */
    private static String getNewRequestMapping(String id) {
        return "/users/" + id + "/settings";
    }


    /** Error (input) tests. */


    /**
     * Left for further development.
     *
     * Not sure what functionality that already exists thanks to
     * tomcat and so on. Might be a good idea to handle the rest
     * with some informative http status code.
     * eg. @RequestMapping(method = { RequestMapping.A, RM.B, RM.C}
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
    public void shouldRespondOnUnsupportedId_GET() throws Exception {
        // id < 1, GET
        mockMvc.perform(get(requestMappingInvalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    public void shouldRespondOnUnsupportedId_POST() throws Exception {
        // id < 1, POST
        mockMvc.perform(post(requestMappingInvalidID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json_test_string))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    public void shouldRespondOnUnsupportedId_PUT() throws Exception {
        // id < 1, PUT
        mockMvc.perform(put(requestMappingInvalidID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json_test_string))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    public void shouldRespondOnUnsupportedId_DELETE() throws Exception {
        // id < 1, DELETE
        mockMvc.perform(delete(requestMappingInvalidID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void shouldHandleInternalServerError_GET() throws Exception {
        // simulate .getUser returning null
        UserDAO ud = new UserDAO();
        UserDAO aSpy = Mockito.spy(ud);
        doReturn(null).when(aSpy).getUser(any(String.class), any(Integer.class));

        mockMvc.perform(get(requestMapping))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldHandleInternalServerError_POST() throws Exception {
        // simulate .getUser returning null
        UserDAO ud = new UserDAO();
        UserDAO aSpy = Mockito.spy(ud);
        doReturn(null).when(aSpy).getUser(any(String.class), any(Integer.class));

        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldHandleInternalServerError_POST_2() throws Exception {
        // simulate .setUserConfig returning false
        SettingsDAO sd = new SettingsDAO();
        SettingsDAO aSpy = Mockito.spy(sd);
        doReturn(false).when(aSpy).setUserConfig(any(Integer.class), any(Settings.class));

        mockMvc.perform(post(requestMapping)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldHandleInternalServerError_PUT() throws Exception {
        // simulate .getUser returning null
        UserDAO ud = new UserDAO();
        UserDAO aSpy = Mockito.spy(ud);
        doReturn(null).when(aSpy).getUser(any(String.class), any(Integer.class));
        mockMvc.perform(put(requestMapping)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json_test_string))
                .andExpect(status().isInternalServerError());
    }


    /** TODO: Result (return) tests. */

    /*
    @Test
    public void shouldRespondCorrectly_GET() throws Exception {
        // use get and compare result with expected
        User user = new User();
        user.setEmail("test@email.test");

        UserDAO ud = new UserDAO();
        UserDAO aSpy = Mockito.spy(ud);
        doReturn(user).when(aSpy).getUser(any(String.class), any(Integer.class));

        SettingsDAO sd = new SettingsDAO();
        SettingsDAO anotherSpy = Mockito.spy(sd);
        doReturn(settings).when(anotherSpy).getUserConfig(any(Integer.class));


        mockMvc.perform(get(requestMapping)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(json_test_string))
                .andExpect(status().isOk());


        //java.lang.AssertionError: Response content
        //Expected :{"language":"english","email":"test@email.test","mailUpdate":true,"textSize":12}
        //Actual   :Could not get user from DAO.

    }
    */
}
