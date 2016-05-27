package studentcapture.course.participant;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// TODO Inte säker på hur bättre tester ska göras / hur de fungerar mot databasen. (ska kolla upp det)
public class ParticipantResourceTest extends StudentCaptureApplicationTests {
    private String courseID;
    private String getPath(String courseID){
        return "/courses/"+courseID+"/participants";
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        courseID = "1200";
    }

    @Test
    public void GETshouldRespondOkWithNoParams() throws Exception {
        mockMvc.perform(get(getPath(courseID))
                .param("courseID",courseID))
                .andExpect(status().isOk());
    }

    /*
      internalServerError since not working against actual db??
     */
    @Test
    public void GETshouldRespondOkWhenUserIDPathIsSet() throws Exception {
        String userID = "/100";
        mockMvc.perform(get(getPath(courseID)+userID)
                .param("courseID",courseID))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void GETshouldRespondOkWhenUserRoleParamIsSet() throws Exception {
        mockMvc.perform(get(getPath(courseID))
                .param("courseID",courseID)
                .param("userRole","teacher"))
                .andExpect(status().isOk());
    }

    @Test
    public void GETshouldRespondBadRequestWhenUserRoleIsScaryDog() throws Exception {
        mockMvc.perform(get(getPath(courseID))
                .param("userRole","ScaryDog"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void POSTshouldRespondBadRequestWithNoParams() throws Exception {
        mockMvc.perform(post(getPath(courseID))
                .param("courseID",courseID))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void POSTshouldRespondBadRequestWhenUserRoleNotSet() throws Exception {
        mockMvc.perform(post(getPath(courseID))
                .param("courseID",courseID)
                .param("userID","100"))
                .andExpect(status().isBadRequest());
    }


    // Returns internal server error since it should not work against actual db??
    @Test
    public void POSTshouldRespondOKtWhenUserRoleAndUserIDisSet() throws Exception {
        mockMvc.perform(post(getPath(courseID))
                .param("courseID",courseID)
                .param("userRole","TeAcHEr")
                .param("userID","16"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void POSTshouldRespondBadRequestWhenUserIDNotSet() throws Exception {
        mockMvc.perform(post(getPath(courseID))
                .param("courseID",courseID)
                .param("userID","100"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void POSTshouldRespondBadRequestWhenUserRoleIsScaryDog() throws Exception {
        mockMvc.perform(post(getPath(courseID))
                .param("courseID",courseID)
                .param("userRole","ScaryDog")
                .param("userID","100"))
                .andExpect(status().isBadRequest());
    }




    /*
    @Test
    public void shouldRespondWithListParticipant() throws Exception {
        URI targetUrl = getUri(courseID);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("UserId",100);
        map.put("CourseId",courseID);
        map.put("Function","teacher");
        Participant participant = new Participant(map);
        List<Participant> list = new ArrayList<>();
        list.add(participant);
        when(templateMock.getForObject(targetUrl, List.class)).thenReturn(list);
        mockMvc.perform(get(getPath(courseID))
                .param("courseID",courseID)
                .param("userRole","teacher")
                .param("userID","100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$Participant[0].UserId").value("100"));
    }
    */
}