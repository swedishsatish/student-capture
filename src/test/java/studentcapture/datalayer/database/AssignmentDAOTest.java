package studentcapture.datalayer.database;

import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import studentcapture.assignment.AssignmentModel;
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

    private static boolean setUpIsDone = false;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Before
    public void setUp() {

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
        AssignmentModel am = new AssignmentModel("PVT", "",
                180, 360, currentDatePlusDaysGenerator(2),
                currentDatePlusDaysGenerator(3),
                currentDatePlusDaysGenerator(1),"U_O_K_G", "");

        int assID = assignmentDAO.createAssignment("UA502", am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());

        assertNotNull(assID);
        assertTrue(assID > 0);
    }

    @Test
    public void shouldBeCorrectDataInRow() throws Exception {
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(2);
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = 180;
        int maxTime = 360;
        String published = currentDatePlusDaysGenerator(1);
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);

        String getAssignmentStatement = "SELECT * FROM "
                + "Assignment WHERE AssignmentId=?;";

        Object[] parameters = new Object[] { new Integer(assID) };
        SqlRowSet srs = jdbcMock.queryForRowSet(getAssignmentStatement, parameters);

        while (srs.next()) {
            assertEquals(courseID, srs.getString("CourseID"));
            assertEquals(assignmentTitle, srs.getString("Title"));
            assertEquals(startDate, srs.getString("StartDate").replaceAll("\\.\\d+", ""));
            assertEquals(endDate, srs.getString("EndDate").replaceAll("\\.\\d+", ""));
            assertEquals(minTime, srs.getInt("MinTime"));
            assertEquals(maxTime, srs.getInt("MaxTime"));
            assertEquals(published, srs.getString("Published").replaceAll("\\.\\d+", ""));
            assertEquals(gradeScale, srs.getString("GradeScale"));
        }
    }

    @Test
    public void shouldCreateTwoAssignments(){
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(2);
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = 180;
        int maxTime = 360;
        String published = currentDatePlusDaysGenerator(1);
        String gradeScale = "U_O_K_G";

        int assID1 = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);
        int assID2 = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);

        assertNotEquals(assID1, assID2);
    }

    @Test
    public void shouldCreateAssignmentWithoutPublishdate(){
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(2);
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = 180;
        int maxTime = 360;
        String published = null;
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);

        String getAssignmentStatement = "SELECT * FROM "
                + "Assignment WHERE AssignmentId=?;";

        Object[] parameters = new Object[] { new Integer(assID) };
        SqlRowSet srs = jdbcMock.queryForRowSet(getAssignmentStatement, parameters);

        while (srs.next()) {
            assertEquals(courseID, srs.getString("CourseID"));
            assertEquals(assignmentTitle, srs.getString("Title"));
            assertEquals(startDate, srs.getString("StartDate").replaceAll("\\.\\d+", ""));
            assertEquals(endDate, srs.getString("EndDate").replaceAll("\\.\\d+", ""));
            assertEquals(minTime, srs.getInt("MinTime"));
            assertEquals(maxTime, srs.getInt("MaxTime"));
            assertNull(srs.getString("Published"));
            assertEquals(gradeScale, srs.getString("GradeScale"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenPublishdateIsBeforeCurrentdate(){
        //Faulty publishdate
        LocalDateTime publishDateTime = LocalDateTime.now().minusDays(1);

        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(2);
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = 180;
        int maxTime = 360;
        String published = publishDateTime.format(formatter);
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithFaultyDates() throws Exception {
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = "20161019 111212"; // Faulty date format
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = 180;
        int maxTime = 360;
        String published = currentDatePlusDaysGenerator(1);
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEndDateIsBeforeStartDate(){
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(3);
        String endDate = currentDatePlusDaysGenerator(2);
        int minTime = 180;
        int maxTime = 360;
        String published = currentDatePlusDaysGenerator(1);
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeMinGreaterMaxTime() throws Exception {
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(2);
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = 300; // Faulty when minTime is greater than maxTime
        int maxTime = 210;
        String published = currentDatePlusDaysGenerator(1);
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeNegative() throws Exception {
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(2);
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = -1; // Faulty when time is negative
        int maxTime = 60;
        String published = currentDatePlusDaysGenerator(1);
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeZero() throws Exception {
        String courseID = "UA502";
        String assignmentTitle = "PVT";
        String startDate = currentDatePlusDaysGenerator(2);
        String endDate = currentDatePlusDaysGenerator(3);
        int minTime = 10;
        int maxTime = 0; //Faulty when time is zero
        String published = currentDatePlusDaysGenerator(1);
        String gradeScale = "U_O_K_G";

        int assID = assignmentDAO.createAssignment(courseID, assignmentTitle, startDate,
                endDate, minTime, maxTime, published, gradeScale);
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
