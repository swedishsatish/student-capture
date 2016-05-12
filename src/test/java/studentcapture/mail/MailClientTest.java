package studentcapture.mail;

import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Class:       MailClientTest
 * <p>
 * Author:      Isak Hjelt, Emil Vannebäck
 * cs-user:     dv14iht, c13evk
 * Date:        5/12/16
 */
public class MailClientTest {

    MailClient mail;
    String subject;
    String body;

    @Before
    public void setUp() throws Exception {
        mail = new MailClient();
        Mailbox.clearAll();
    }

    @Test
    public void testSendSingleMail() throws Exception {
        subject = "test";
        body = "123";
        mail.send("vanneback@hotmail.com", "c13evk@cs.umu.se", subject, body);
        List<Message> inbox = Mailbox.get("vanneback@hotmail.com");
        assertTrue(inbox.size() == 1);
    }

    @Test
    public void testSubject() throws MessagingException {
        subject = "test subject";
        body = "";
        mail.send("test.dest@nutpan.com", "test.src@nutpan.com", subject, body);
        List<Message> inbox = Mailbox.get("test.dest@nutpan.com");
        System.out.println(inbox.get(0).getSubject());
        assertEquals("test subject",inbox.get(0).getSubject());
    }

    @Test
    public void testBody() throws MessagingException, IOException {
        subject = "";
        body = "test body";
        mail.send("test.dest@nutpan.com", "test.src@nutpan.com", subject, body);
        List<Message> inbox = Mailbox.get("test.dest@nutpan.com");
        assertEquals("test body",inbox.get(0).getContent());
    }


    @Test
    public void testRandomAmountOfMails() throws AddressException {
        subject = "";
        body = "";
        Random genRand = new Random();
        int rand = genRand.nextInt(10) + 2;
        for (int i=0; i < rand; i++) {
            mail.send("test.dest@nutpan.com", "test.src@nutpan.com", subject, body);
        }
        List<Message> inbox = Mailbox.get("test.dest@nutpan.com");
        assertEquals(rand,inbox.size());
    }
}