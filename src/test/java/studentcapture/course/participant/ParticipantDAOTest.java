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
        String sql1 = "INSERT INTO Users VALUES (1, 'pelle', 'pelle', 'pellesson', 'pelle@gmail.com', 'MyPassword',null);";
        String sql2 = "INSERT INTO Users VALUES (5, 'alle', 'alle','allesson', 'alle@gmail.com', 'MyPassword',null);";
        String sql3 = "INSERT INTO Course VALUES (2,2016,'VT', '1234', 'ABC', true);";
        String sql4 = "INSERT INTO Participant VALUES (1,2, 'student');";
        String sql5 = "INSERT INTO Participant VALUES (5,2, 'teacher');";
        String sql10 = "INSERT INTO Users VALUES (10, 'kalle', 'Carl', 'carlsson', 'calle@gmail.com', 'MyPassword',null);";


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

    @Test
    public void testGetAllParticipants() {
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants(2,"all roles");
        assertTrue(participants.get().size() == 2);
        Participant p1 = participants.get().get(0);
        Participant p2 = participants.get().get(1);
        assertTrue(p2.getCourseId().compareTo(2) == 0);
        assertEquals(p2.getCourseId(),p1.getCourseId());
        assertTrue(p1.getUserId() == 1 || p2.getUserId() == 1);
        assertTrue((p1.getUserId() + p2.getUserId()) == 6);

    }

    @Test
    public void testGetAllTeachers() {
        String role = "teacher";
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants(2,role);
        assertTrue(participants.get().size() == 1);
        Participant p1 = participants.get().get(0);
        assertEquals((Integer)5,p1.getUserId());
        assertEquals(p1.getFunction(),role);
    }


    @Test
    public void testGetSingleUser() {
        Optional<Participant> participant = participantDAO.getCourseParticipant(2,5);
        assertEquals((Integer)5,participant.get().getUserId());
        assertEquals(participant.get().getFunction(),"teacher");

        participant = participantDAO.getCourseParticipant(2,1);
        assertEquals((Integer)1,participant.get().getUserId());
        assertEquals(participant.get().getFunction(),"student");
    }


    @Test
    public void testGetUsersWithRoleAsScaryDog() {
        String role = "ScaryDog";
        Optional<List<Participant>> participants = participantDAO.getCourseParticipants(2,role);
        assertFalse(participants.isPresent());
    }
}