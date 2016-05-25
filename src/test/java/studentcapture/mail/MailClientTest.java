package studentcapture.mail;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.jvnet.mock_javamail.Mailbox;
import org.springframework.beans.factory.annotation.Autowired;
import studentcapture.config.StudentCaptureApplicationTests;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class:       MailClientTest
 * <p/>
 * Author:      Isak Hjelt, Emil Vannebäck
 * cs-user:     dv14iht, c13evk
 * Date:        5/12/16
 */
public class MailClientTest extends StudentCaptureApplicationTests {

    @Autowired
    MailClient mail;

    String subject;
    String body;

    @Before
    public void setUp() throws Exception {
        Mailbox.clearAll();
    }

    @After
    public void tearDown() throws Exception {
        Mailbox.clearAll();
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testNullSubjectAndBody() throws Exception {
        mail.send("test.dest@hotmail.com", "test.scr", null, null);
    }

    @Test
    public void testNullTo() throws Exception {
        subject = "fff";
        body = "222";

        exception.expect(Exception.class);
        mail.send(null, "test.scr", subject, body);
    }

    @Test
    public void testNullFrom() throws Exception {
        subject = "fff";
        body = "222";

        exception.expect(Exception.class);
        mail.send("test.dest@hotmail.com", null, subject, body);
    }

    @Test
    public void testSendSingleMail() throws Exception {
        subject = "test";
        body = "123";
        mail.send("test.dest@hotmail.com", "test.scr@hotmail.com", subject, body);
        List<Message> inbox = Mailbox.get("test.dest@hotmail.com");
        assertTrue(inbox.size() == 1);
    }

    @Test
    public void testSubject() throws MessagingException {
        subject = "test subject";
        body = "";
        mail.send("test.dest@nutpan.com", "test.src@nutpan.com", subject, body);
        List<Message> inbox = Mailbox.get("test.dest@nutpan.com");
        assertEquals("test subject", inbox.get(0).getSubject());
    }

    @Test
    public void testBody() throws MessagingException, IOException {
        subject = "";
        body = "test body";
        mail.send("test.dest@nutpan.com", "test.src@nutpan.com", subject, body);
        List<Message> inbox = Mailbox.get("test.dest@nutpan.com");
        assertEquals("test body", inbox.get(0).getContent());
    }

    @Test
    public void testRandomAmountOfMails() throws AddressException {
        subject = "";
        body = "";
        Random genRand = new Random();
        int rand = genRand.nextInt(10) + 2;
        for (int i = 0; i < rand; i++) {
            mail.send("test.dest@nutpan.com", "test.src@nutpan.com", subject, body);
        }
        List<Message> inbox = Mailbox.get("test.dest@nutpan.com");
        assertEquals(rand, inbox.size());
    }
}