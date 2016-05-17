package studentcapture.datalayer.database;

import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import studentcapture.assignment.AssignmentDAO;
import studentcapture.assignment.AssignmentDateIntervalls;
import studentcapture.assignment.AssignmentModel;
import studentcapture.assignment.AssignmentVideoIntervall;
import studentcapture.config.StudentCaptureApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;


/**
 * Created by S&E on 4/27/16.
 */
public class AssignmentDAOTest extends StudentCaptureApplicationTests {

    @Autowired
    AssignmentDAO assignmentDAO;

    @Autowired
    private JdbcTemplate jdbcMock;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd HH:mm:ss");
    private AssignmentModel am;
    private String courseID = "UA502";
    private String getAssignmentStatement = "SELECT * FROM "
            + "Assignment WHERE AssignmentId=?;";

    @Before
    public void setUp() {
        AssignmentVideoIntervall videoIntervall = new AssignmentVideoIntervall();
        AssignmentDateIntervalls assignmentIntervalls = new AssignmentDateIntervalls();

        videoIntervall.setMinTimeSeconds(180);
        videoIntervall.setMaxTimeSeconds(360);
        assignmentIntervalls.setStartDate(currentDatePlusDaysGenerator(2));
        assignmentIntervalls.setEndDate(currentDatePlusDaysGenerator(3));
        assignmentIntervalls.setPublishedDate(currentDatePlusDaysGenerator(1));
        am = new AssignmentModel("PVT", //Title
                "", // Info
                videoIntervall,
                assignmentIntervalls,
                "U_O_K_G", // GradeScale
                ""); // Recap
        am.setCourseID(courseID);
        String sql = "INSERT INTO Course VALUES ('UA502', 1912, 'HT12', " +
                "'ht1212', 'Comedy','Description' , true);";
        jdbcMock.execute(sql);
    }

    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Course;";
        String sql2 = "DELETE FROM Assignment;";

        jdbcMock.update(sql2);
        jdbcMock.update(sql1);
    }

    @Test
    public void shouldCreateAssignment() throws Exception {
        int assID = assignmentDAO.createAssignment(am);

        assertNotNull(assID);
        assertTrue(assID > 0);
    }

    @Test
    public void shouldBeCorrectDataInRow() throws Exception {

        int assID = assignmentDAO.createAssignment(am);

        SqlRowSet srs = getRowSetFromAssignment(assID);

        while (srs.next()) {
            assertEquals(courseID, srs.getString("CourseID"));
            assertEquals(am.getTitle(), srs.getString("Title"));
            assertEquals(am.getAssignmentIntervall().getStartDate(),
                        srs.getString("StartDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getAssignmentIntervall().getEndDate(),
                        srs.getString("EndDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getVideoIntervall().getMinTimeSeconds(), srs.getInt("MinTime"));
            assertEquals(am.getVideoIntervall().getMaxTimeSeconds(), srs.getInt("MaxTime"));
            assertEquals(am.getAssignmentIntervall().getPublishedDate(),
                        srs.getString("Published").replaceAll("\\.\\d+", ""));
            assertEquals(am.getScale(), srs.getString("GradeScale"));
        }
    }

    @Test
    public void shouldCreateTwoAssignments(){
        int assID1 = assignmentDAO.createAssignment(am);
        int assID2 = assignmentDAO.createAssignment(am);

        assertNotEquals(assID1, assID2);
    }

    @Test
    public void shouldCreateAssignmentWithoutPublishdate(){
        am.getAssignmentIntervall().setPublishedDate(null);

        int assID = assignmentDAO.createAssignment(am);

        SqlRowSet srs = getRowSetFromAssignment(assID);

        while (srs.next()) {
            assertEquals(courseID, srs.getString("CourseID"));
            assertEquals(am.getTitle(), srs.getString("Title"));
            assertEquals(am.getAssignmentIntervall().getStartDate(),
                        srs.getString("StartDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getAssignmentIntervall().getEndDate(),
                        srs.getString("EndDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getVideoIntervall().getMinTimeSeconds(), srs.getInt("MinTime"));
            assertEquals(am.getVideoIntervall().getMaxTimeSeconds(), srs.getInt("MaxTime"));
            assertNull(srs.getString("Published"));
            assertEquals(am.getScale(), srs.getString("GradeScale"));
        }
    }

    private String currentDatePlusDaysGenerator(int days){
        return LocalDateTime.now().plusDays(days).format(formatter);
    }

    private SqlRowSet getRowSetFromAssignment(int assID) {
        Object[] parameters = new Object[]{new Integer(assID)};
        SqlRowSet srs = jdbcMock.queryForRowSet(getAssignmentStatement, parameters);
        return srs;
    }

    /*
    @org.junit.Test
    public void testGetAssignmentInfo() throws Exception {

    }*/

    /*@Test
    public void testUpdateAssignment() throws Exception {

    }

    @Test
    public void testRemoveAssignment() throws Exception {

    }*/
}
