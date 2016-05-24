package studentcapture.mail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import studentcapture.config.StudentCaptureApplicationTests;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Class:       MailDAOTest
 * <p/>
 *
 * A class for testing mailDAO.
 *
 * Author:      Isak Hjelt, Emil Vannebäck
 * cs-user:     dv14iht, c13evk
 * Date:        5/18/16
 */
public class MailDAOTest extends StudentCaptureApplicationTests{
    @Autowired
    protected JdbcTemplate jdbcMock;

    @Autowired
    MailDAO mailDAO;

    /**
     * initiates test database
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        String sql1 = "INSERT INTO Users VALUES (1, 'mkyong', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword',null);";
        String sql2 = "INSERT INTO Users VALUES (2, 'alex', 'abcd', 'defg', 'alex@yahoo.com', 'SecretPassword', null);";
        String sql3 = "INSERT INTO Users VALUES (3, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword', null);";
        String sql4 = "INSERT INTO Course VALUES (1, 2016, 'VT', 'ABC', null, true);";
        String sql5 = "INSERT INTO Assignment VALUES (1, 1, 'OU1', '2016-05-13 10:00:00', '2016-05-13 12:00:00', 60, 180, null, 'XYZ');";
        String sql6 = "INSERT INTO Submission VALUES (1, 1, null, null, '2016-05-13 11:00:00', null, null, null, null);";
        String sql7 = "INSERT INTO Submission VALUES (1, 3, null, null, '2016-05-13 11:00:00', 'MVG', 2, null, null);";
        String sql8 = "INSERT INTO Participant VALUES (3, 1, 'Student');";
        String sql9 = "INSERT INTO Participant VALUES (2, 1, 'Student');";

        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
        jdbcMock.update(sql3);
        jdbcMock.update(sql4);
        jdbcMock.update(sql5);
        jdbcMock.update(sql6);
        jdbcMock.update(sql7);
        jdbcMock.update(sql8);
        jdbcMock.update(sql9);

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
    public void testGetParticipantsEmails(){

        List<String> emailList = mailDAO.getPraticipantsEmails("1").get();
        assertEquals(2,emailList.size());
        assertEquals("joel@gmail.com", emailList.get(0));
    }

    @Test
    public void testGetStartDateFromAssignment(){
        assertEquals("2016-05-13 10:00:00.0",mailDAO.getStartDateFromAssignment("1").get());
    }

    @Test
    public void  testGetCourseIDFromAssignment(){
        assertEquals("1",mailDAO.getCourseIDFromAssignment("1").get());
    }

    @Test
    public void testWrongAssignmentID(){
        assertEquals(Optional.empty(),mailDAO.getStartDateFromAssignment("12345"));
    }


}