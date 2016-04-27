package studentcapture.feedback;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by c12osn on 2016-04-26.
 */
public class FeedbackControllerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RestTemplate templateMock;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void shouldBeAbleToHandleSimpleGetRequest() throws Exception {
        mockMvc.perform(get("/feedback/get"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldBeAbleToHandleGetRequestWithParams() throws Exception {
        mockMvc.perform(get("/feedback/get")
                .param("name", "Anna"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRespondWithFeedback() throws Exception {
        mockMvc.perform(get("/feedback/get")
                .param("name", "Anna")
                .param("course", "PVT")
                .param("exam", "Exam 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedback").value("vg"));
    }

    @Test
    public void shouldRespondWithErrorIfNoFeedback() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("courseID", "2");
        params.put("studentID", "3");
        params.put("examID", "1");

        when(templateMock.getForObject("http://localhost:8080/DB/getGrade", String.class, params)).thenReturn("{grade:VG}");
        System.out.println("HELOLOLOLLOLLOLL");
        System.out.println(System.identityHashCode(templateMock));

        mockMvc.perform(get("/feedback/get")
                .param("name", "Olle")
                .param("course", "PVT")
                .param("exam", "Exam 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("VG"));
    }
}