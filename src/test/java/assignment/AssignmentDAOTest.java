package assignment;

import javassist.NotFoundException;
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
    private int courseID = 502;

    @Before
    public void setUp() {
        AssignmentVideoIntervall videoIntervall = new AssignmentVideoIntervall();
        AssignmentDateIntervalls assignmentIntervalls = new AssignmentDateIntervalls();

        videoIntervall.setMinTimeSeconds(180);
        videoIntervall.setMaxTimeSeconds(360);
        assignmentIntervalls.setStartDate(currentDatePlusDaysGenerator(2));
        assignmentIntervalls.setEndDate(currentDatePlusDaysGenerator(3));
        assignmentIntervalls.setPublishedDate(currentDatePlusDaysGenerator(1));
        am = new AssignmentModel(
                courseID,               // CourseId
                "PVT",                  //Title
                "",                     // Info
                videoIntervall,
                assignmentIntervalls,
                "U_O_K_G",              // GradeScale
                "");                    // Recap
        am.setCourseID(courseID);
        String sql = "INSERT INTO Course VALUES (502, 1912, 'HT12', " +
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
    public void shouldCreateTwoAssignments() throws Exception {
        int assID1 = assignmentDAO.createAssignment(am);
        int assID2 = assignmentDAO.createAssignment(am);

        assertNotEquals(assID1, assID2);
    }

    @Test
    public void shouldCreateAssignmentWithoutPublishdate() throws Exception {
        am.getAssignmentIntervall().setPublishedDate(null);

        int assID = assignmentDAO.createAssignment(am);

        assertEquals(am, assignmentDAO.getAssignmentModel(assID));
    }

    @Test
    public void shouldGetCorrectAssignment() throws Exception {
        int assID = assignmentDAO.createAssignment(am);

        assertEquals(am, assignmentDAO.getAssignmentModel(assID));
    }

    @Test
    public void shouldNotGetCorrectAssignment() throws Exception {
        int assID = assignmentDAO.createAssignment(am);

        am.setTitle("");

        assertNotEquals(am, assignmentDAO.getAssignmentModel(assID));
    }

    @Test (expected = NotFoundException.class)
    public void shouldNotGetNonExistingAssignment() throws Exception {
        int assID = assignmentDAO.createAssignment(am);
        int noneAssID = assID + 500;

        assignmentDAO.getAssignmentModel(noneAssID);
    }

    @Test (expected = NotFoundException.class)
    public void shouldNotGetDeletedAssignment() throws Exception{
        int assID = assignmentDAO.createAssignment(am);

        assignmentDAO.removeAssignment(151, assID);

        assignmentDAO.getAssignmentModel(assID);
    }

    @Test
    public void shouldDeleteOneAnAssignment() throws Exception{
        int assID = assignmentDAO.createAssignment(am);

        assertTrue(assignmentDAO.removeAssignment(151, assID));
    }

    @Test
    public void shouldNotDeleteAnAssignment() throws Exception {
        int assID = assignmentDAO.createAssignment(am);
        int noneAssID = assID + 500;

        assertFalse(assignmentDAO.removeAssignment(151, noneAssID));
    }

    private String currentDatePlusDaysGenerator(int days){
        return LocalDateTime.now().plusDays(days).format(formatter);
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
