package studentcapture.help;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studentcapture.administrator.AdministratorDAO;
import studentcapture.mail.MailClient;

/**
 * Created by g8 on 2016-05-24.
 *
 */
@RestController
@RequestMapping(value = "/help")
public class HelpResource {

    @Autowired
    AdministratorDAO administratorDAO;

    //@Autowired
    //@Qualifier("helpMailClient")
    private MailClient mailClient = new MailClient();

    /**
     * Get the FAQ.
     * @return ?
     */
    @CrossOrigin
    @RequestMapping(
            value = "/faq",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String getFAQ() {
        return "FAQ sample text";
    }


    /**
     * Take the contact form and email it to the administrator email
     * that is located in the DB.
     *
     * IMPORTANT:
     * No guarantee at the moment that a real email is located in the
     * database, therefore a temp email is used to develop.
     *
     * @param scContactForm Form information.
     * @return              200 OK (but does not guarantee that the mail is sent),
     *                      500 Internal Server Error.
     */
    @CrossOrigin
    @RequestMapping(
            value = "/form",
            method = RequestMethod.PUT
    )
    public ResponseEntity<String> mailContactForm(
            @RequestBody SCContactForm scContactForm) {

        String to;
        String from = scContactForm.getSenderEmail();
        String subject = "Contact Form";
        String message;

        /*
        // See method doc.
        to = administratorDAO.getSupportEmail();
        if (to == null) {
            return new ResponseEntity<String>(
                    "No administrator mail found",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        */

        /* Temp email. */
        to = "c13bbd@cs.umu.se";

        message = formatMessage(scContactForm);
        mailClient.send(to, from, subject, message);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    /**
     * Make it easier to edit the form to email and the above
     * easier to read.
     * @param scContactForm Form information.
     * @return The message to email.
     */
    private String formatMessage(SCContactForm scContactForm) {
        return "Student Capture Contact Form:\n"
                + "OS: " + scContactForm.getSenderOperatingSystem() + "\n"
                + "Browser: " + scContactForm.getSenderBrowser() + "\n"
                + "---\n"
                + scContactForm.getSenderMessage();
    }
}
