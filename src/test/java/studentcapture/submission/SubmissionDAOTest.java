package studentcapture.submission;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.model.Grade;
import studentcapture.submission.Submission;
import studentcapture.submission.SubmissionDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by c13arm on 2016-05-12.
 */
public class SubmissionDAOTest extends StudentCaptureApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Autowired
    SubmissionDAO submissionDAO;

    private Submission subWithoutGrade = new Submission();
    private Submission subWithGrade = new Submission();

    /**
     * Inserts dummy data to test against h2Database
     */
    @Before
    public void setUp() {
        String sql1 = "INSERT INTO Users VALUES (1, 'mkyong', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword', null);";
        String sql2 = "INSERT INTO Users VALUES (2, 'alex', 'abcd', 'defg', 'alex@yahoo.com', 'SecretPassword', null);";
        String sql3 = "INSERT INTO Users VALUES (3, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword', null);";
        String sql4 = "INSERT INTO Users VALUES (4, 'username', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword', null);";
        String sql5 = "INSERT INTO Course VALUES (1, 2016, 'VT', 'ABC', null, true);";
        String sql6 = "INSERT INTO Assignment VALUES (1, 1, 'OU1', '2016-05-13 10:00:00', '2016-05-13 12:00:00', 60, 180, null, 'XYZ');";
        String sql7 = "INSERT INTO Submission VALUES (1, 1, null, null, '2016-05-13 11:00:00', null, null, null, null, null);";
        String sql8 = createSubmission(subWithoutGrade, false, 3);
        String sql9 = createSubmission(subWithGrade, true, 4);
        String sql10 = "INSERT INTO Participant VALUES (3, 1, 'Teacher');";

        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
        jdbcMock.update(sql3);
        jdbcMock.update(sql4);
        jdbcMock.update(sql5);
        jdbcMock.update(sql6);
        jdbcMock.update(sql7);
        jdbcMock.update(sql8);
        jdbcMock.update(sql9);
        jdbcMock.update(sql10);
    }


    /**
     * A helpsetup-method to create the correct SQL string and create the
     * corresponding submissionobject.
     * @param submission The submission object that will be the same as the
     *                   submission object that comes from the database.
     * @param isGraded A boolean to tell the helpmethod if the submission
     *                 in the database will be graded or not.
     * @param studentID The student ID that has done the submission, must
     *                  exist in the Users table in the database.
     * @return The SQL string that can be plugged into the database.
     */
    private String createSubmission(Submission submission, boolean isGraded, int studentID) {
        Grade grade = new Grade();
        String gradeString;
        Integer teacherId;
        String teacherName;

        int assignmentID = 1;
        boolean studentPublishConsent = false;
        String status = null;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        if (isGraded) {
            gradeString = "MVG";
            teacherId = 2;
            teacherName = "abcd defg";
            grade.setTeacherID(teacherId);
            grade.setGrade(gradeString);
            gradeString = "'" + gradeString + "'";
        } else {
            gradeString = null;
            teacherId = null;
            teacherName = null;
            grade.setTeacherID(teacherId);
            grade.setGrade(gradeString);
        }
        boolean publishStudentSubmission = false;
        boolean publishFeedback = false;
        grade.setFeedbackIsVisible(publishFeedback);

        submission.setAssignmentID(assignmentID);
        submission.setStudentID(studentID);
        submission.setStudentPublishConsent(studentPublishConsent);
        submission.setStatus(status);
        submission.setSubmissionDate(timestamp);
        submission.setGrade(grade);
        submission.setPublishStudentSubmission(publishStudentSubmission);
        submission.setTeacherName(teacherName);
        submission.setFeedback("");

        String SQL = "INSERT INTO Submission VALUES ("  + assignmentID + ", "
                                                        + studentID + ", "
                                                        + studentPublishConsent + ", "
                                                        + status + ", "
                                                        + "'" + timestamp + "'" + ", "
                                                        + gradeString + ", "
                                                        + teacherId + ", "
                                                        + publishStudentSubmission + ", "
                                                        + publishFeedback + ", null)";

        return SQL;
    }

    /**
     * Remove all content from the database
     */
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

    @Test
    public void addSubmissionReturnTrue() {
        Submission submission= new Submission(2,1);
        assertTrue(submissionDAO.addSubmission(submission,true));
    }

    /*
    @Test
    public void addSubmissionReturnFalse() {
        Submission submission= new Submission(4,1);
        assertFalse(submissionDAO.addSubmission(submission,true));
    }
    */

    /**
     * Tests that the insertion of test data works
     */
    @Test
    @SuppressWarnings("unchecked")
    public void databaseSetUpTest() {
        String sql = "SELECT * FROM Submission WHERE assignmentID = 1 AND studentID = 1";

        //Getting values from table Submission
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcMock.queryForObject(sql, new SubmissionDAOWrapper());

        assertEquals("1",info.get("AssignmentId"));
        assertEquals("1",info.get("StudentId"));
        assertEquals(null,info.get("StudentPublishConsent"));
        assertEquals("2016-05-13 11:00:00.0",info.get("SubmissionDate"));
    }

    @Test
    public void shouldGetCorrectNumberOfSubmissions() throws Exception {
        List<Submission> submissions = submissionDAO.getAllSubmissions(1);

        assertEquals(3 ,submissions.size());
    }

    /**
     * Tests the correct insertion of values from method setGrade
     */
    @Test
    @SuppressWarnings("unchecked")
    public void setGradeValues() throws IllegalAccessException {
        /*
        createSubmission(Submission submission, boolean isGraded, int studentID) {
        String sql1 = "INSERT INTO Users VALUES (1, 'mkyong', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword', null);";
        String sql2 = "INSERT INTO Users VALUES (2, 'alex', 'abcd', 'defg', 'alex@yahoo.com', 'SecretPassword', null);";
        String sql3 = "INSERT INTO Users VALUES (3, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword', null);";
        String sql4 = "INSERT INTO Users VALUES (4, 'username', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword', null);";
        String sql5 = "INSERT INTO Course VALUES (1, 2016, 'VT', 'ABC', null, true);";
        String sql6 = "INSERT INTO Assignment VALUES (1, 1, 'OU1', '2016-05-13 10:00:00', '2016-05-13 12:00:00', 60, 180, null, 'XYZ');";
        String sql7 = "INSERT INTO Submission VALUES (1, 1, null, null, '2016-05-13 11:00:00', null, null, null, null);";
        String sql8 = createSubmission(subWithoutGrade, false, 3);
        String sql9 = createSubmission(subWithGrade, true, 4);
        String sql10 = "INSERT INTO Participant VALUES (3, 1, 'Teacher');";
         */
        int userId = 3;
        String course = "1";
        int assId = 1;
        int studentId = 1;
        int teacherId = 3;
        String gradeStr = "vg";

        Submission submission = new Submission(studentId, assId);
        Grade grade = new Grade(gradeStr, teacherId);
        submission.setGrade(grade);
        submission.setCourseID(course);

        submissionDAO.setGrade(submission, userId);

        String sql = "SELECT * FROM Submission WHERE assignmentID = 1 AND studentID = 1";

        //Getting values from table Submission
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcMock.queryForObject(sql, new SubmissionDAOWrapper());

        assertEquals("1",info.get("AssignmentId"));
        assertEquals("1",info.get("StudentId"));
        assertEquals(null,info.get("StudentPublishConsent"));
        assertEquals("vg",info.get("Grade"));
        assertEquals("3",info.get("TeacherId"));
    }

    /**
     * Checks the returnvalue from an insertion with correct values
     */
    @Test
    public void gradeExistingAssignment() throws IllegalAccessException {
        Submission submission = new Submission(1,1);
        Grade grade = new Grade("vg", 3);
        submission.setCourseID("1");
        submission.setGrade(grade);

        boolean returnValue = submissionDAO.setGrade(submission, 3);

        assertTrue(returnValue);
    }

    /**
     * Checks the returnvalue from an insertion with incorrect values
     */
    @Test
    public void gradeNonExistingAssignment() throws IllegalAccessException {
        Submission submission = new Submission(2,1);
        Grade grade = new Grade("vg", 3);
        submission.setCourseID("1");
        submission.setGrade(grade);

        boolean returnValue = submissionDAO.setGrade(submission, 3);

        assertFalse(returnValue);
    }

    /**
     * Checks if the teacher trying to set a grade exists in the table, in this test the teacher does not exist and the test should return false
     */
    //TODO ändrade illegalAccesException till DataIntegretyViolationException... så att testet skulle fungera, inte säker på hur det ska vara
    @Test(expected = IllegalAccessException.class)
    public void nonExistingTeacherSetsGrade() throws IllegalAccessException {
        Submission submission = new Submission(1,1);
        Grade grade = new Grade("vg", 2);
        submission.setCourseID("1");
        submission.setGrade(grade);

        boolean returnValue = submissionDAO.setGrade(submission, 7777777);
    }

    /**
     * Teacher cannot publish non-graded feedback
     */
    @Test
    public void publishNonGradedFeedback() throws IllegalAccessException {
        Submission submission = new Submission(1,1);
        submission.setCourseID("PVT");
        boolean returnValue = submissionDAO.publishFeedback(submission, true);

        assertFalse(returnValue);
    }

    /**
     * Teacher publishes feedback
     */
    @Test
    public void publishFeedback() throws IllegalAccessException {
        Submission submission = new Submission(1,1);
        Grade grade = new Grade("vg", 3);
        submission.setCourseID("1");
        submission.setGrade(grade);

        boolean returnValue = submissionDAO.publishFeedback(submission, true);

        assertTrue(returnValue);
    }

    @Test
    public void shouldReturnSubmissionWithGrade() {
        Optional<Submission> result = submissionDAO.getSubmission(1, 4);
        Submission submissionFromDB = result.get();

        assertTrue(result.isPresent());
        assertTrue(subWithGrade.equals(submissionFromDB));
    }

    @Test
    public void shouldReturnSubmissionWithoutGrade() {
        Optional<Submission> result = submissionDAO.getSubmission(1, 3);
        Submission submissionFromDB = result.get();

        assertTrue(result.isPresent());
        assertTrue(subWithoutGrade.equals(submissionFromDB));
    }

    /**
     *  Used to collect user information, and return a hashmap.
     */
    protected class SubmissionDAOWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public HashMap<String, String> mapRow(ResultSet rs, int i) throws SQLException {
            HashMap<String,String> info = new HashMap<>();
            info.put("AssignmentId",rs.getString("AssignmentId"));
            info.put("StudentId",rs.getString("StudentId"));
            info.put("StudentPublishConsent",rs.getString("StudentPublishConsent"));
            info.put("SubmissionDate",rs.getString("SubmissionDate"));
            info.put("Grade",rs.getString("Grade"));
            info.put("TeacherId",rs.getString("TeacherId"));
            info.put("PublishStudentSubmission",rs.getString("PublishStudentSubmission"));
            return info;
        }
    }
}
