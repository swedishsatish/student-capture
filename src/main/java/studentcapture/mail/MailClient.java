package studentcapture.mail;



import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Class:       BasicSanitizer
 * <p>
 * Author:      Isak Hjelt
 * cs-user:     dv14iht
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

    public void send(String to, String from, String subject, String msg) {

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
