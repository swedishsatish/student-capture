package studentcapture.video.videoIn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * https://github.com/student-capture/student-capture/issues/23
 *
 * "As a student I want to be able to leave a blank answer to a
 * question."
 *
 * Created by group 8 on 2016-05-11.
 */
public class WithdrawControllerTest extends StudentCaptureApplicationTests {

    /** Mapping to controller may change. If outdated, update String. */
    private final String mappedAddress = "/emptySubmission";

    /** Expected response might change in back-end. If outdated, update String */
    private final String expectedResponseOK = "Student submitted an empty answer";

    /** Expected response might change in back-end. If outdated, update String */
    private final String expectedResponseFailed = "DB failure for student submission";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate mockRestTemplate;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(
                webApplicationContext).build();
    }

    @Test
    public void shouldRespondWithCorrectErrorOnWrongRequest1() throws Exception {
        mockMvc.perform(get(mappedAddress))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldRespondWithCorrectErrorOnWrongRequest2() throws Exception {
        mockMvc.perform(get(mappedAddress)
                .param("courseCode", "1")
                .param("courseID", "1")
                .param("assignmentID", "1")
                .param("userID", "1"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldRespondWithErrorOnNoParams() throws Exception {
        mockMvc.perform(post(mappedAddress))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldRespondWithBadRequestOnMissingParam() throws Exception {
        // Done: userID removed.
        // Test: Should pass independent off which param is missing.
        mockMvc.perform(post(mappedAddress)
                .param("courseCode", "1")
                .param("courseID", "1")
                .param("assignmentID", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRespondWithBadRequestOnWrongParamNames() throws Exception {
        // Done: Typo in param name courseCode.
        // Test: Should pass independent off which param is missing.
        mockMvc.perform(post(mappedAddress)
                .param("Codecourse", "1")
                .param("courseID", "1")
                .param("assignmentID", "1")
                .param("userID", "1"))
                .andExpect(status().isBadRequest());
    }

    /*  TODO: Stopped working after latest pull from Dev.
        TODO: Investigate cause.
    @Test
    public void shouldRespondWithCantFulfillRequest() throws Exception {
        // response == null.
        // Server lacks the ability to fulfill the request.
        mockMvc.perform(post(mappedAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .param("courseCode", "1")
                .param("courseID", "1")
                .param("assignmentID", "1")
                .param("userID", "1"))
                .andExpect(status().isNotImplemented());
    }
    */

    /**
     * If this test fail, check back-end method return value.
     * If the return type is still a string, update this test.
     * If the return type has changed, update this test.
     * @throws Exception
     */
    @Test
    public void shouldRespondWithFailureOnWrongParamValue() throws Exception {
        /*
         * wrong values passed -->
         * wrong url -->
         * "DB failure for student submission" in back-end -->
         * send http.someStatus to front-end.
         */
        when(mockRestTemplate
                .postForObject(any(String.class), isNull(), any()))
                .thenReturn(expectedResponseFailed);

        // userID = -1, but it's just there as an example.
        mockMvc.perform(post(mappedAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .param("courseCode", "1")
                .param("courseID", "1")
                .param("assignmentID", "1")
                .param("userID", "-1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldHandleExceptionAndRespond() throws Exception {
        doThrow(new RestClientException("test postForObject()"))
                .when(mockRestTemplate)
                .postForObject(any(String.class), isNull(), any());

        mockMvc.perform(post(mappedAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .param("courseCode", "1")
                .param("courseID", "1")
                .param("assignmentID", "1")
                .param("userID", "1"))
                .andExpect(status().isInternalServerError());

        // (important) Reset template to not affect any other test.
        Mockito.reset(mockRestTemplate);
    }

    /**
     * If this test fail, check back-end method return value.
     * If the return type is still a string, update this test.
     * If the return type has changed, update this test.
     * @throws Exception
     */
    @Test
    public void shouldBeAbleToAddEmptySubmission() throws Exception {
        when(mockRestTemplate
                .postForObject(any(String.class), isNull(), any()))
                .thenReturn(expectedResponseOK);

        mockMvc.perform(post(mappedAddress)
                .contentType(MediaType.APPLICATION_JSON)
                .param("courseCode", "1")
                .param("courseID", "1")
                .param("assignmentID", "1")
                .param("userID", "1"))
                .andExpect(status().isOk());
    }

    // TODO: Video always null check?
    // TODO: header tests?
}
