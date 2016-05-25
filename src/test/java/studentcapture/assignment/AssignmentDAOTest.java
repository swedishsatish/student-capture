package studentcapture.assignment;

import javassist.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import studentcapture.config.StudentCaptureApplicationTests;

import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;


public class AssignmentDAOTest extends StudentCaptureApplicationTests {

    @Autowired
    AssignmentDAO assignmentDAO;

    @Autowired
    private JdbcTemplate jdbcMock;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");
    private AssignmentModel am;

    @Before
    public void setUp() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertCourseQueryString = "INSERT INTO Course " +
                "(Year, Term, CourseName, CourseDescription, Active) " +
                "VALUES (1912, 'HT12', 'Comedy','Description' , true);";
        jdbcMock.update(
                connection -> {
                    return connection.prepareStatement(insertCourseQueryString,
                                    Statement.RETURN_GENERATED_KEYS);
                },
                keyHolder);

        int courseID;
        if (keyHolder.getKeys().size() > 1) {
            courseID = (int) keyHolder.getKeys().get("courseid");
        } else {
            courseID = keyHolder.getKey().intValue();
        }

        AssignmentVideoIntervall videoIntervall = new AssignmentVideoIntervall();
        AssignmentDateIntervalls assignmentIntervalls = new AssignmentDateIntervalls();
        videoIntervall.setMinTimeSeconds(180);
        videoIntervall.setMaxTimeSeconds(360);
        assignmentIntervalls.setStartDate(currentDatePlusDaysGenerator(2));
        assignmentIntervalls.setEndDate(currentDatePlusDaysGenerator(3));
        assignmentIntervalls.setPublishedDate(currentDatePlusDaysGenerator(1));
        am = new AssignmentModel(
                courseID,               // CourseId
                "PVT",                  // Title
                "Description",          // Info
                videoIntervall,
                assignmentIntervalls,
                "U_O_K_G",              // GradeScale
                "Recap");               // Recap
        am.setCourseID(courseID);
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

        assertEquals(am, assignmentDAO.getAssignmentModel(assID).get());
    }

    @Test
    public void shouldGetCorrectAssignment() throws Exception {
        int assID = assignmentDAO.createAssignment(am);

        assertEquals(am, assignmentDAO.getAssignmentModel(assID).get());
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

    /**
     * Should update assignment in the database.
     */
    @Test
    public void shouldUpdateAssignmentConfigurations() throws Exception {
        // Setup: create ass, set new description
        int assID = assignmentDAO.createAssignment(am);
        am.setAssignmentID(assID);
        String originalDescription = am.getDescription();
        String updatedDescription = "Updated description";
        am.setDescription(updatedDescription);

        // Update and get updated
        assignmentDAO.updateAssignment(am);
        AssignmentModel am2 = assignmentDAO.getAssignmentModel(am.getAssignmentID()).get();

        // Assert that truly updated
        assertNotEquals(am2.getDescription(), originalDescription);
        assertEquals(am2.getDescription(), updatedDescription);
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