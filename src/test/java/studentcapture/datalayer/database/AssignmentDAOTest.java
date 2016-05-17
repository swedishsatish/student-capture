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

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd HH:mm:ss");
    private AssignmentModel am;
    private String courseID = "UA502";
    private String getAssignmentStatement = "SELECT * FROM "
            + "Assignment WHERE AssignmentId=?;";

    @Before
    public void setUp() {
        am = new AssignmentModel("PVT", //Title
                "", // Info
                180, // MinTime
                360, // MaxTime
                currentDatePlusDaysGenerator(2), // StartDate
                currentDatePlusDaysGenerator(3), // EndDate
                currentDatePlusDaysGenerator(1), // PublishDate
                "U_O_K_G", // GradeScale
                ""); // Recap

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
        //AssignmentModel am = new AssignmentModel("PVT", "",
        //        180, 360, currentDatePlusDaysGenerator(2),
        //        currentDatePlusDaysGenerator(3),
        //        currentDatePlusDaysGenerator(1),"U_O_K_G", "");

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());

        assertNotNull(assID);
        assertTrue(assID > 0);
    }

    @Test
    public void shouldBeCorrectDataInRow() throws Exception {

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());

        SqlRowSet srs = getRowSetFromAssignment(assID);

        while (srs.next()) {
            assertEquals(courseID, srs.getString("CourseID"));
            assertEquals(am.getTitle(), srs.getString("Title"));
            assertEquals(am.getStartDate(),
                        srs.getString("StartDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getEndDate(),
                        srs.getString("EndDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getMinTimeSeconds(), srs.getInt("MinTime"));
            assertEquals(am.getMaxTimeSeconds(), srs.getInt("MaxTime"));
            assertEquals(am.getPublished(),
                        srs.getString("Published").replaceAll("\\.\\d+", ""));
            assertEquals(am.getScale(), srs.getString("GradeScale"));
        }
    }

    @Test
    public void shouldCreateTwoAssignments(){
        int assID1 = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());
        int assID2 = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());

        assertNotEquals(assID1, assID2);
    }

    @Test
    public void shouldCreateAssignmentWithoutPublishdate(){
        am.setPublished(null);

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());

        SqlRowSet srs = getRowSetFromAssignment(assID);

        while (srs.next()) {
            assertEquals(courseID, srs.getString("CourseID"));
            assertEquals(am.getTitle(), srs.getString("Title"));
            assertEquals(am.getStartDate(),
                        srs.getString("StartDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getEndDate(),
                        srs.getString("EndDate").replaceAll("\\.\\d+", ""));
            assertEquals(am.getMinTimeSeconds(), srs.getInt("MinTime"));
            assertEquals(am.getMaxTimeSeconds(), srs.getInt("MaxTime"));
            assertNull(srs.getString("Published"));
            assertEquals(am.getScale(), srs.getString("GradeScale"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenPublishdateIsBeforeCurrentdate(){
        //Faulty publishdate
        LocalDateTime publishDateTime = LocalDateTime.now().minusDays(1);
        String published = publishDateTime.format(formatter);
        am.setPublished(published);

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithFaultyDates() throws Exception {
        String startDate = "20161019 111212"; // Faulty date format

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                startDate, am.getEndDate(), am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenEndDateIsBeforeStartDate(){
        String startDate = currentDatePlusDaysGenerator(3);
        String endDate = currentDatePlusDaysGenerator(2); // enddate is one day
                                                          // before startdate
        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                startDate, endDate, am.getMinTimeSeconds(),
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeMinGreaterMaxTime() throws Exception {
        int minTime = 300; // Faulty when minTime is greater than maxTime
        int maxTime = 210;

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), minTime,
                maxTime, am.getPublished(), am.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeNegative() throws Exception {
        int minTime = -1; // Faulty when time is negative

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), minTime,
                am.getMaxTimeSeconds(), am.getPublished(), am.getScale());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreateWithTimeZero() throws Exception {
        int maxTime = 0; //Faulty when time is zero

        int assID = assignmentDAO.createAssignment(courseID, am.getTitle(),
                am.getStartDate(), am.getEndDate(), am.getMinTimeSeconds(),
                maxTime, am.getPublished(), am.getScale());
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
