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
import org.springframework.web.util.UriComponentsBuilder;
import studentcapture.config.StudentCaptureApplicationTests;

import java.net.URI;
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
        URI targetUrl = UriComponentsBuilder.fromUriString("https://localhost:8443")
                .path("DB/getGrade")
                .queryParam("userID", "Anna")
                .queryParam("assID", "1")
                .build()
                .toUri();

        when(templateMock.getForObject(targetUrl, String.class)).thenReturn("{grade:VG, feedback:Mycket fint ritat}");
        mockMvc.perform(get("/feedback/get")
                .param("userID", "Anna")
                .param("assID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("VG"))
                .andExpect(jsonPath("$.feedback").value("Mycket fint ritat"));
    }

}