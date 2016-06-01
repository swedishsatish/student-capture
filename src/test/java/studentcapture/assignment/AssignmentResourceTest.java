package studentcapture.assignment;

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
import org.thymeleaf.util.DateUtils;
import studentcapture.assignment.GradeScale;
import studentcapture.config.H2DB;
import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.course.CourseDAO;
import studentcapture.course.CourseDAOTest;
import studentcapture.course.CourseModel;
import studentcapture.assignment.AssignmentDAO;
import studentcapture.assignment.AssignmentDateIntervalls;
import studentcapture.assignment.AssignmentModel;
import studentcapture.assignment.AssignmentVideoIntervall;
import studentcapture.course.participant.Participant;
import studentcapture.course.participant.ParticipantDAO;
import studentcapture.user.User;
import studentcapture.user.UserDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private ParticipantDAO participandDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AssignmentDAO assignmentDao;

    @Autowired
    private JdbcTemplate jdbcMock;

    private int assignment1;
    private int assignment2;


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

        tearDown();


        /*String sql3 = "INSERT INTO Users VALUES (3, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword', null, false);";
        String sql4 = "INSERT INTO Users VALUES (4, 'derp', 'abcd', 'defg', 'derp@gmail.com', 'MyGloriousPassword', null);";

        String sql5 = "INSERT INTO Course VALUES (1, 2016, 'VT', 'ABC', null, true);";

        String sql6 = "INSERT INTO Assignment VALUES (1, 1, 'OU1', '2016-10-25 10:00:00', '2016-10-25 12:00:00', 60, 180, null, 'IG_G_VG_MVG')";
        String sql7 = "INSERT INTO Assignment VALUES (2, 1, 'OU1', '2016-6-25 10:00:00', '2016-10-25 12:00:00', 60, 180, null, 'IG_G_VG_MVG')";

        String sql10 = "INSERT INTO Participant VALUES (3, 1, 'Teacher');";

        jdbcMock.update(sql3);
        jdbcMock.update(sql4);
        jdbcMock.update(sql5);
        jdbcMock.update(sql6);
        jdbcMock.update(sql10);*/

        userDAO.addUser(new User("lalal", "Test", "Testsson", "test@test.test", "uberpassword", false));
        userDAO.addUser(new User("lalfdfal", "Tfdest", "Tedfstsson", "test@tefdst.test", "uberfpassword", false));


        CourseModel courseModel = new CourseModel();
        courseModel.setCourseName("HerpDerp");
        courseModel.setActive(true);
        courseModel.setTerm("HT16");
        courseModel.setYear(2016);
        courseModel.setCourseDescription("DPEPREWSPGSGSG");

        CourseModel course = courseDAO.addCourse(courseModel);


        AssignmentModel model = new AssignmentModel();
        model.setTitle("HUEHUEUEh");
        model.setCourseID(course.getCourseId());
        model.setScale("IG_G_VG_MVG");
        model.setRecap("HEUHUEHEH");
        model.setDescription("HUEHUEHUEUHEUH");
        AssignmentDateIntervalls interval = new AssignmentDateIntervalls();

        AssignmentVideoIntervall vintervlal = new AssignmentVideoIntervall();
        vintervlal.setMaxTimeSeconds(500);
        vintervlal.setMinTimeSeconds(100);

        DateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = null;
        try {
            currentDate = sdf1.parse(new Date().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date future = new Date(currentDate.getTime() + TimeUnit.HOURS.toMillis(1));
        Date back = new Date(currentDate.getTime() - TimeUnit.HOURS.toMillis(1));

        interval.setEndDate(sdf2.format(future));
        interval.setStartDate(sdf2.format(back));
        interval.setPublishedDate(sdf2.format(back));

        model.setAssignmentIntervall(interval);
        model.setVideoIntervall(vintervlal);



        AssignmentModel model2 = new AssignmentModel();
        model2.setTitle("Assignment 2");
        model2.setCourseID(course.getCourseId());
        model2.setScale("IG_G_VG_MVG");
        model2.setRecap("HEUHUEHEH");
        model2.setDescription("HUEHUEHUEUHEUH");
        AssignmentDateIntervalls interval2 = new AssignmentDateIntervalls();

        AssignmentVideoIntervall vintervlal2 = new AssignmentVideoIntervall();
        vintervlal2.setMaxTimeSeconds(500);
        vintervlal2.setMinTimeSeconds(100);

        interval2.setEndDate("2016-05-26 13:00:00");
        interval2.setStartDate("2016-05-26 10:00:00");
        interval2.setPublishedDate("2016-05-26 10:00:00");
        model2.setAssignmentIntervall(interval2);
        model2.setVideoIntervall(vintervlal2);




        try {


            this.assignment1 = assignmentDao.createAssignment(model);
            this.assignment2 = assignmentDao.createAssignment(model2);
        } catch (IOException e) {
            e.toString();
        }



        Participant part = new Participant();
        part.setCourseId(course.getCourseId());
        part.setUserId(1);
        part.setFunction("Teacher");
        participandDAO.addParticipant(part);



    }


    @After
    public void tearDown() {
        try {
            H2DB.TearDownDB(jdbcMock);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    @Test
    public void shouldSendNotFound() throws Exception {


        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userid", "2");

        mvc.perform(get("/assignments/" + this.assignment1).sessionAttrs(sessionAttrs)).andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnOk() throws  Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userid", "1");

        mvc.perform(get("/assignments/" + this.assignment1).sessionAttrs(sessionAttrs)).andExpect(status().isOk());
    }

    @Test
    public void shouldNotAcceptAccessingAssignmentVideoOutsideTimeframe() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userid", "1");

        mvc.perform(get("/assignments/" + this.assignment2 + "/video").sessionAttrs(sessionAttrs)).andExpect(status().isNotFound());

    }

    @Test
    public void shouldAcceptAccessingAssignmentVideoInsideTimeframe() throws Exception {
        Map<String, Object> sessionAttrs = new HashMap<>();
        sessionAttrs.put("userid", "1");

        mvc.perform(get("/assignments/" + this.assignment1 +  "/video").sessionAttrs(sessionAttrs)).andExpect(status().isOk());

    }

    private void printUsersTableTemp(String table) {
        System.out.println("!!!!!!!!!!!!!!!!!TABLE!!!!!!!!!!!!!!!!!!!!!!!!!\n");

        String sql = "SELECT * FROM " + table;
        List<Map<String,Object>> users = jdbcMock.queryForList(sql);

        if(users != null && !users.isEmpty()) {
            for(Map<String,Object> user: users) {
                for(Iterator<Map.Entry<String, Object>> it = user.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String,Object> entry = it.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key + " = " + value);
                }
                System.out.println();
            }
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

    }

}
