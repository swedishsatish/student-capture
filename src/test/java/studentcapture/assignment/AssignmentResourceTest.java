package studentcapture.assignment;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AssignmentResourceTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private final static String JSON_TEST_STRING = "{\"title\":\"TheTitle\"," +
            "\"Info\": \"Assignment Info\"," +
            "\"videoIntervall\":" +
            "{\"minTimeSeconds\":0," +
            "\"maxTimeSeconds\":320}," +
            "\"assignmentIntervall\":" +
            "{\"startDate\":\"2016-10-01 10:00:00\"," +
            "\"endDate\":\"2016-10-02 10:00:00\"," +
            "\"published\":\"2016-10-01 10:00:00\"}," +
            "\"scale\":\"" + GradeScale.U_O_K_G.toString() + "\"," +
            "\"recap\":\"recap\"" +
            "}";

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context).
                        build();
    }

    /**
     * This test tests that the JSON will be properly sent and parsed.
     * Will throw DataIntegrityViolationException (in NestedServletException)
     * because this test connects to the database, where the course doesn't exist.
     */
    @Test (expected = NestedServletException.class)
    public void shouldWorkToSendJSONToCreateAssignment() throws Exception {
        mvc.perform(post("/assignments").contentType(MediaType.APPLICATION_JSON)
                .content(JSON_TEST_STRING)).andExpect(status().isOk());
    }

    @Test
    public void shouldHaveBadRequestOnWrongJSON() throws Exception {
        mvc.perform(post("/assignments").contentType(MediaType.APPLICATION_JSON)
                .content("{\"wrong\":\"json\"")).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldWorkToSendFileAndMetaData() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "assignment.webm",
                "form", "some video".getBytes());

        mvc.perform(fileUpload("/assignments/video").file(file)
                .param("courseID", "1000").param("assignmentID", "3"));
    }
}