package studentcapture.feedback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;
import studentcapture.config.StudentCaptureApplicationTests;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class FeedbackControllerTest extends StudentCaptureApplicationTests {


    private URI getUri() {
        return UriComponentsBuilder.fromUriString("https://localhost:8443")
                .path("DB/getGrade")
                .queryParam("studentID", "1")
                .queryParam("assignmentID", "1")
                .queryParam("courseID", "1")
                .build()
                .toUri();
    }

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
    public void shouldRespondWithErrorWithoutParams() throws Exception {
        mockMvc.perform(get("/feedback/get")).andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldRespondOkWithParams() throws Exception {
        mockMvc.perform(get("/feedback/get")
                .param("studentID", "1")
                .param("assignmentID", "1")
                .param("courseID", "1")).andExpect(status().isOk());
    }

    @Test
    public void shouldRespondWithFeedback() throws Exception {
        URI targetUrl = getUri();
        HashMap response = new HashMap();
        response.put("teacher", "Kalle");
        response.put("time", "13:37:00 1993");
        response.put("grade", "MVG");
        when(templateMock.getForObject(targetUrl, HashMap.class)).thenReturn(response);
        mockMvc.perform(get("/feedback/get")
                .param("studentID", "1")
                .param("assignmentID", "1")
                .param("courseID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("MVG"))
                .andExpect(jsonPath("$.teacher").value("Kalle"))
                .andExpect(jsonPath("$.time").value("13:37:00 1993"));
    }

    @Test
    public void shouldReturnErrorWhenRequestFail() throws Exception {
        URI targetUrl = getUri();

        when(templateMock.getForObject(targetUrl, HashMap.class)).thenThrow(new RestClientException("Exception message"));
        mockMvc.perform(get("/feedback/get")
                .param("studentID", "1")
                .param("assignmentID", "1")
                .param("courseID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Exception message"));
    }


    @Test
    public void shouldReturnJsonForValidRequest() throws Exception {
        URI targetUrl = getUri();
        when(templateMock.getForObject(targetUrl, HashMap.class)).thenReturn(new HashMap());
        mockMvc.perform(get("/feedback/get")
                .param("studentID", "1")
                .param("assignmentID", "1")
                .param("courseID", "1"))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnJsonOnError() throws Exception {
        URI targetUrl = getUri();
        when(templateMock.getForObject(targetUrl, HashMap.class)).thenThrow(new RestClientException("Exception message"));
        mockMvc.perform(get("/feedback/get")
                .param("studentID", "1")
                .param("assignmentID", "1")
                .param("courseID", "1"))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorWithoutVideoParams() throws Exception {
        mockMvc.perform(get("/feedback/video"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnVideo() throws Exception {
        URI targetUrl = UriComponentsBuilder.fromUriString("https://localhost:8443/videoDownload/")
                .path("1/1/1/1/")
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity(headers);

        when(templateMock.exchange(targetUrl, HttpMethod.GET, entity, Object.class)).thenReturn(null);
        mockMvc.perform(get("/feedback/video")
                .param("studentID", "1")
                .param("assignmentID", "1")
                .param("courseID", "1")
                .param("courseCode", "1"))
                .andExpect(status().isOk());
    }
    @Test
    public void shouldReturnEmptyOnVideoRequestError() throws Exception {
        URI targetUrl = UriComponentsBuilder.fromUriString("https://localhost:8443/videoDownload/")
                .path("1/1/1/1/")
                .build()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity(headers);

        when(templateMock.exchange(targetUrl, HttpMethod.GET, entity, Object.class)).thenThrow(new RestClientException("error"));
        MvcResult res = mockMvc.perform(get("/feedback/video")
                                .param("studentID", "1")
                                .param("assignmentID", "1")
                                .param("courseID", "1")
                                .param("courseCode", "1"))
                                .andExpect(status().isOk()).andReturn();
        assertEquals(res.getResponse().getContentAsString(), "");
    }
}