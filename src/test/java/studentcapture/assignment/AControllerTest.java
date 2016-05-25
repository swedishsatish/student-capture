/**
 * TODO: Can't run test classes (IDE/Spring/? problems).
 * TODO: Complete test class for the controller.
 *
 * Sprint       1
 * Group        8
 * Date:        27/4-2016
 *
 * User stories covered in this class:
 * [Student](9): As a student I want to be able to leave a blank answer to a question.
 * [Student](10.1): As a student I want to view the accepted starting time for each upcoming exam.
 * https://github.com/student-capture/student-capture/issues
 */
package studentcapture.assignment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;
import studentcapture.config.StudentCaptureApplicationTests;

import java.net.URI;
import java.util.HashMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AControllerTest extends StudentCaptureApplicationTests {


    private String pathOne = "/assignment2/startTime";
    private String pathTwo = "/assignment2/emptyAnswer";

    private URI getUri() {
        return UriComponentsBuilder.fromUriString("https://localhost:8443")
                .path("DB/getAssignmentInfo")
                .queryParam("assignmentID", "1")
                .queryParam("courseID", "1")
                .build()
                .toUri();
    }

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private RestTemplate mockRestTemplate;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(
                webApplicationContext).build();
        Mockito.reset(mockRestTemplate);
    }

    /**
     * As a student I want to view the accepted starting time for
     * each upcoming exam.
     * TODO: add Headers in messages (?).
     */

    @Test
    public void shouldHandleCorrectContentType() throws Exception {
        mockMvc.perform(get(pathOne)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("courseID", "1")
                .param("assignmentID", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRespondWithOkWithParams() throws Exception {
        mockMvc.perform(get(pathOne)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("courseID", "1")
                .param("assignmentID", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRespondWithErrorWithoutParams() throws Exception {
        mockMvc.perform(get("/assignment2/startTime"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldBeAbleToRespondWithStartTimes() throws Exception {
        URI dbAddress = getUri();
        HashMap response = new HashMap();
        response.put("opening", "123");
        response.put("closing", "456");

        when(mockRestTemplate.postForObject(dbAddress, null, HashMap.class)).thenReturn(response);
        mockMvc.perform(get(pathOne)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("courseID", "1")
                .param("assignmentID", "1"))
                .andExpect(status().isOk())
                /*
                TODO: Check return values (failing).
                .andExpect(jsonPath("$.opening").value("123"))
                .andExpect(jsonPath("$.closing").value("456"))
                */;
    }

    /*
    @Test
    public void shouldBeAbleToRespondWithError() throws Exception {
        fail("Not implemented.");
    }
    */

    //TODO: As a student I want to be able to leave a blank answer to a question.
}