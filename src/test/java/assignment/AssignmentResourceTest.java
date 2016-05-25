package assignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import studentcapture.assignment.GradeScale;
import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.course.CourseDAO;
import studentcapture.course.CourseDAOTest;
import studentcapture.course.CourseModel;
import studentcapture.assignment.AssignmentDAO;
import studentcapture.assignment.AssignmentDateIntervalls;
import studentcapture.assignment.AssignmentModel;
import studentcapture.assignment.AssignmentVideoIntervall;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by root on 5/4/16.
 */
public class AssignmentResourceTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;
@Autowired
    private CourseDAO courseDAO;

    @Autowired
    private AssignmentDAO assignmentDao;

    @Autowired
    private JdbcTemplate jdbcMock;

    private String json_test_string = "{\"title\":\"TheTitle\"," +
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


        String sql3 = "INSERT INTO Users VALUES (3, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword', null);";
        String sql4 = "INSERT INTO Users VALUES (4, 'derp', 'abcd', 'defg', 'derp@gmail.com', 'MyGloriousPassword', null);";


        String sql5 = "INSERT INTO Course VALUES (1, 2016, 'VT', 'ABC', null, true);";

        String sql6 = "INSERT INTO Assignment VALUES (1, 1, 'OU1', '2016-10-25 10:00:00', '2016-10-25 12:00:00', 60, 180, null, 'U_G_VG_MVG');";
        String sql10 = "INSERT INTO Participant VALUES (3, 1, 'Teacher');";

        jdbcMock.update(sql3);
        jdbcMock.update(sql4);
        jdbcMock.update(sql5);
        jdbcMock.update(sql6);
        jdbcMock.update(sql10);

    }


    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Users;";
        String sql2 = "DELETE FROM Course;";
        String sql3 = "DELETE FROM Assignment;";
        String sql4 = "DELETE FROM Submission;";
        String sql5 = "DELETE FROM Participant;";

        jdbcMock.update(sql5);
        jdbcMock.update(sql4);
        jdbcMock.update(sql3);
        jdbcMock.update(sql2);
        jdbcMock.update(sql1);
    }


    /**
     * This test tests that the JSON will be properly sent and parsed.
     * Will throw DataIntegrityViolationException (in NestedServletException)
     * because this test connects to the database, where the course does not exist.
     */
    @Test (expected = NestedServletException.class)
    public void shouldWorkToSendJSONToCreateAssignment() throws Exception {
        mvc.perform(post("/assignments").contentType(MediaType.APPLICATION_JSON).content(json_test_string))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldHaveBadRequestOnWrongJSON() throws Exception {
        mvc.perform(post("/assignments").contentType(MediaType.APPLICATION_JSON).content("{\"wrong\":\"json\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldWorkToSendFileAndMetaData() throws Exception {
        MockMultipartFile file = new MockMultipartFile("data", "assignment.webm", "form", "some video".getBytes());
        HashMap<String, String> contentTypeParams = new HashMap<String, String>();

        mvc.perform(fileUpload("/assignments/video").file(file).param("courseID", "1000").param("assignmentID", "3"));
    }

    @Test
    public void shouldSendNotFound() throws Exception {


        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userid", "4");

        mvc.perform(get("/assignments/1").sessionAttrs(sessionAttrs)).andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnOk() throws  Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userid", "3");

        mvc.perform(get("/assignments/1").sessionAttrs(sessionAttrs)).andExpect(status().isOk());
    }

    @Test
    public void shouldNotAcceptAccessingAssignmentTooEarly() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userid", "3");

        mvc.perform(get("/assignments/1/video").sessionAttrs(sessionAttrs)).andExpect(status().isNotFound());

    }

}
