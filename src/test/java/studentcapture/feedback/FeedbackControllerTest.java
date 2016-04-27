package studentcapture.feedback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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


public class FeedbackControllerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RestTemplate templateMock;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.reset(templateMock);
    }

    @Test
    public void shouldRespondToGet() throws Exception {
        mockMvc.perform(get("/feedback/get")).andExpect(status().isOk());
    }

    @Test
    public void shouldRespondWithFeedback() throws Exception {
        HashMap<String, String> params = new HashMap<>();
        params.put("courseID", "1");
        params.put("studentID", "2");
        params.put("examID", "3");
        when(templateMock.getForObject("http://localhost:8080/DB/getGrade", String.class, params)).thenReturn("{grade:VG, feedback:Mycket fint ritat}");
        mockMvc.perform(get("/feedback/get")
                .param("courseID", "1")
                .param("studentID", "2")
                .param("examID", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("VG"))
                .andExpect(jsonPath("$.feedback").value("Mycket fint ritat"));
    }

}