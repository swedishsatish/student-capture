package studentcapture.help;

/**
 * POJO class for student capture contact form data.
 *
 * Created by g8 on 2016-05-24.
 */
public class SCContactForm {

    private String senderEmail;
    private String senderOperatingSystem;
    private String senderBrowser;
    private String senderMessage;

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderOperatingSystem() {
        return senderOperatingSystem;
    }

    public void setSenderOperatingSystem(String senderOperatingSystem) {
        this.senderOperatingSystem = senderOperatingSystem;
    }

    public String getSenderBrowser() {
        return senderBrowser;
    }

    public void setSenderBrowser(String senderBrowser) {
        this.senderBrowser = senderBrowser;
    }

    public String getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }
}
