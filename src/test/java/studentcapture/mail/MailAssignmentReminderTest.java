package studentcapture.mail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.Message;
import javax.mail.internet.AddressException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.*;

/**
 * @author Isak Hjelt, Emil Vannebäck
 * cs-user:     dv14iht, c13evk
 * Date:        5/12/16
 */

public class MailAssignmentReminderTest extends StudentCaptureApplicationTests {
    @Autowired
    MailAssignmentReminder mailReminder;

    @Autowired
    JdbcTemplate jdbcMock;

    @Autowired
    MailDAO mailDAO;

    @Before
    public void setUp() throws Exception {
        String sql1 = "INSERT INTO Users VALUES (1, 'mkyong', 'abcd', 'defg', 'mkyong@gmail.com', 'MyPassword');";
        String sql2 = "INSERT INTO Users VALUES (2, 'alex', 'abcd', 'defg', 'alex@yahoo.com', 'SecretPassword');";
        String sql3 = "INSERT INTO Users VALUES (3, 'joel', 'abcd', 'defg', 'joel@gmail.com', 'MyGloriousPassword');";
        String sql4 = "INSERT INTO Course VALUES ('PVT', 2016, 'VT', '1234', 'ABC', null, true);";
        String sql5 = "INSERT INTO Assignment VALUES (1, 'PVT', 'OU1', '2016-05-13 10:00:00', '2016-05-13 12:00:00', 60, 180, null, 'XYZ');";
        String sql6 = "INSERT INTO Submission VALUES (1, 1, null, null, '2016-05-13 11:00:00', null, null, null, null);";
        String sql7 = "INSERT INTO Submission VALUES (1, 3, null, null, '2016-05-13 11:00:00', 'MVG', 2, null, null);";
        String sql8 = "INSERT INTO Participant VALUES (3, 'PVT', 'Student');";
        String sql9 = "INSERT INTO Participant VALUES (2, 'PVT', 'Student');";
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

    @After
    public void tearDown() throws Exception {
        Mailbox.clearAll();
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
    public void testSendReminderAll() throws AddressException {
        String courseID;
        List<Message> inbox;
        List<String> emailAddresses;
        boolean gotReminderEmail = true;
        Optional<List<String>> tempOptList;
        Optional <String> tempOptString;

        mailReminder.sendReminderEmail("1");
        tempOptString = mailDAO.getCourseIDFromAssignment("1");

        if (tempOptString.isPresent()) {
            courseID = tempOptString.get();
            tempOptList = mailDAO.getPraticipantsEmails(courseID);

            if (tempOptList.isPresent()) {
                emailAddresses = tempOptList.get();

                for (String emailAddress : emailAddresses) {

                    inbox = Mailbox.get(emailAddress);
                    if (inbox.size() != 1) {
                        gotReminderEmail = false;
                        break;
                    }
                }
            }
        }
        assertTrue(gotReminderEmail);
    }

    @Test
    public void testSendToBadAssignmentID() throws Exception {
        mailReminder.sendReminderEmail("999999999");
    }
}