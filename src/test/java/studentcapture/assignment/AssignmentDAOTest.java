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

    private final static DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private AssignmentModel am;
    private int testCourseID;

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

        if (keyHolder.getKeys().size() > 1) {
            testCourseID = (int) keyHolder.getKeys().get("courseid");
        } else {
            testCourseID = keyHolder.getKey().intValue();
        }

        AssignmentVideoIntervall videoIntervall =
                new AssignmentVideoIntervall();
        AssignmentDateIntervalls assignmentIntervalls =
                new AssignmentDateIntervalls();
        videoIntervall.setMinTimeSeconds(180);
        videoIntervall.setMaxTimeSeconds(360);
        assignmentIntervalls.setStartDate(currentDatePlusDaysGenerator(2));
        assignmentIntervalls.setEndDate(currentDatePlusDaysGenerator(3));
        assignmentIntervalls.setPublishedDate(currentDatePlusDaysGenerator(1));
        am = new AssignmentModel(
                testCourseID,           // CourseId
                "PVT",                  // Title
                "Description",          // Info
                videoIntervall,
                assignmentIntervalls,
                GradeScale.U_O_K_G.toString(), // GradeScale
                "Recap");               // Recap
        am.setCourseID(testCourseID);
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

        assignmentDAO.removeAssignment(testCourseID, assID);

        assignmentDAO.getAssignmentModel(assID);
    }

    @Test
    public void shouldDeleteOneAnAssignment() throws Exception{
        int assID = assignmentDAO.createAssignment(am);

        assertTrue(assignmentDAO.removeAssignment(testCourseID, assID));
    }

    @Test
    public void shouldNotDeleteAnAssignment() throws Exception {
        int assID = assignmentDAO.createAssignment(am);
        int noneAssID = assID + 500;

        assertFalse(assignmentDAO.removeAssignment(testCourseID, noneAssID));
    }

    private String currentDatePlusDaysGenerator(int days){
        return LocalDateTime.now().plusDays(days).format(FORMATTER);
    }

    /**
     * Should update assignment in the database.
     */
    @Test
    public void shouldUpdateConfigurations() throws Exception {
        // Setup: create ass, set new title
        int assID = assignmentDAO.createAssignment(am);
        am.setAssignmentID(assID);
        String originalTitle = am.getTitle();
        String updatedTitle = "Updated title";
        am.setTitle(updatedTitle);

        // Update and get updated
        assignmentDAO.updateAssignment(am);
        AssignmentModel am2 = assignmentDAO
                .getAssignmentModel(am.getAssignmentID()).get();

        // Assert that truly updated
        assertNotEquals(am2.getTitle(), originalTitle);
        assertEquals(am2.getTitle(), updatedTitle);
    }

    /**
     * Should update assignment configuration files
     * in the file system  (ex recap).
     */
    @Test
    public void shouldUpdateFiles() throws Exception {
        // Setup: create ass, set new recap
        int assID = assignmentDAO.createAssignment(am);
        am.setAssignmentID(assID);
        String originalRecap = am.getRecap();
        String updatedRecap = "Updated recap.";
        am.setRecap(updatedRecap);

        // Update and get updated
        assignmentDAO.updateAssignment(am);
        AssignmentModel am2 = assignmentDAO
                .getAssignmentModel(am.getAssignmentID()).get();

        // Assert that truly updated
        assertNotEquals(am2.getRecap(), originalRecap);
        assertEquals(am2.getRecap(), updatedRecap);
    }

    @Test (expected = NotFoundException.class)
    public void shouldFailToUpdateNonExisting() throws Exception {
        // Setup: create ass, set new title
        int nonExistingAssID = assignmentDAO.createAssignment(am) + 666;
        am.setAssignmentID(nonExistingAssID);
        am.setTitle("Updated title");

        // Try to update
        assignmentDAO.updateAssignment(am);
    }

}