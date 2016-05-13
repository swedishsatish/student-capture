package studentcapture.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Class:       MailClient
 * <p/>
 *
 * A class for sending emails
 *
 * Author:      Isak Hjelt, Emil Vannebäck
 * cs-user:     dv14iht, c13evk
 * Date:        5/11/16
 */
public class MailClient {
    private Properties properties;
    private Session session;

    public MailClient() {
        properties = new Properties();
        properties.put("mail.smtp.host", "mail.cs.umu.se");
        properties.put("mail.smtp.port", "587");
        session = Session.getDefaultInstance(properties);
    }

    /**
     *
     * @param to The address to send the mail to
     * @param from The address you are mailing from
     * @param subject The subject
     * @param msg The body of the email
     */
    public void send(String to, String from, String subject, String msg) {

        if (msg == null) {
            msg = "";
        }

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
