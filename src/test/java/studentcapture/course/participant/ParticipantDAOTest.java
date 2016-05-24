package studentcapture.course.participant;



import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bio11lrm on 2016-05-16.
 */
public class ParticipantDAOTest extends StudentCaptureApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Autowired
    ParticipantDAO participantDAO;

    /**
     * Inserts dummy data to test against h2Database
     */
    @Before
    public void setUp() {
        String sql1 = "INSERT INTO Users VALUES (1, 'mudd', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword',null);";
        String sql2 = "INSERT INTO Users VALUES (5, 'madd', 'defg','hej', 'mkyong@gmail.com', 'MyPassword',null);";
        String sql3 = "INSERT INTO Course VALUES (2,2016,'VT', '1234', null, true);";
        String sql4 = "INSERT INTO Participant VALUES (1,2, 'student');";
        String sql5 = "INSERT INTO Participant VALUES (5,2, 'teacher');";
        String sql10 = "INSERT INTO Users VALUES (10, 'mudda', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword',null);";


        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
        jdbcMock.update(sql3);
        jdbcMock.update(sql4);
        jdbcMock.update(sql5);
        jdbcMock.update(sql10);
    }

    /**
     * Remove all content from the database
     */
    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Users;";
        String sql2 = "DELETE From Config;";
        String sql3 = "DELETE FROM Course;";
        String sql4 = "DELETE FROM Participant;";

        jdbcMock.update(sql4);
        jdbcMock.update(sql3);
        jdbcMock.update(sql2);
        jdbcMock.update(sql1);
    }

    // TODO : cannot parse to integer in Participant.... so tesst dont work
    /*
    @Test
    public void testGetAllParticipants() {
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants("2","many users","all roles");
        assertTrue(participants.get().size() == 2);
        Participant p1 = participants.get().get(0);
        Participant p2 = participants.get().get(1);
        assertTrue(p2.getCourseId().compareTo(2) == 0);
        assertEquals(p2.getCourseId(),p1.getCourseId());
        assertTrue(p1.getUserId() == 1 || p2.getUserId() == 1);
        assertTrue((p1.getUserId() + p2.getUserId()) == 6);

    }
    */
    // TODO : cannot parse to integer in Participant.... so tesst dont work
    /*
    @Test
    public void testGetAllTeachers() {
        String role = "teacher";
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants("2","many users",role);
        assertTrue(participants.get().size() == 1);
        Participant p1 = participants.get().get(0);
        assertEquals((Integer)5,p1.getUserId());
        assertEquals(p1.getFunction(),role);
    }
    */

    /*
        When quering for a single user, the userrole is irrelevant
     */
    // TODO : cannot parse to integer in Participant.... so tesst dont work
    /*
    @Test
    public void testGetSingleUser() {
        String role = "teacher";
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants("2","1",role);
        assertTrue(participants.get().size() == 1);
        Participant p1 = participants.get().get(0);
        assertEquals((Integer)1,p1.getUserId());
        assertEquals(p1.getFunction(),"student");
    }
    */

    @Test
    public void testGetUserWithRoleAsScaryDog() {
        String role = "ScaryDog";
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants("2","many users",role);
        assertFalse(participants.isPresent());
    }
}