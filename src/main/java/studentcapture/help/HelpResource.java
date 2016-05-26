package studentcapture.help;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import studentcapture.administrator.AdministratorDAO;
import studentcapture.mail.MailClient;

/**
 * Rest controller for help related functions like FAQ, contact form.
 *
 * Created by g8 on 2016-05-24.
 */
@RestController
@RequestMapping(value = "/help")
public class HelpResource {

    /** Map the administratorDAO variable to the bean. */
    @Autowired
    AdministratorDAO administratorDAO;

    /*
     * note:
     *
     * Multiple files using the same @Autowired but only one
     * bean is created (?).
     * Could be fixed with @Qualifier to distinguish between
     * different beans of the same class, but then more beans
     * will be created.
     */
    //@Autowired
    //@Qualifier("someIdentifier")
    /** Temporary solution for mail client dependency. */
    private MailClient mailClient = new MailClient();

    /**
     * Should get the FAQ as html file and return to the front-end.
     * @return the FAQ.
     */
    @Deprecated
    @CrossOrigin
    @RequestMapping(
            value = "/faq",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> getFAQ() {
        return new ResponseEntity<>("FAQ sample text", null);
    }

    /**
     * Take the contact form and email it to the administrator email
     * that is located in the database.
     * The sender "nobody@cs.umu.se" is used because according to
     * support@cs it will be thrown in the spam folder directly.
     *
     * @param scContactForm Form information.
     * @return              200 OK,
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
        String from = "nobody@cs.umu.se";
        String subject = "Student Capture Contact Form";
        String message;

        /* Get the email address for the receiver */
        to = administratorDAO.getSupportEmail();
        if (to == null) {
            return new ResponseEntity<>(
                    "No administrator mail found.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /* Format the message and send the email */
        message = formatMessage(scContactForm);
        mailClient.send(to, from, subject, message);

        return new ResponseEntity<>("Mail sent.", HttpStatus.OK);
    }

    /**
     * Make it easier to edit the form message and the calling
     * method easier to read.
     * @param scContactForm Form information.
     * @return The message to email.
     */
    private String formatMessage(SCContactForm scContactForm) {
        return "Student Capture Contact Form:\n"
                + "Response email: " + scContactForm.getSenderEmail() + "\n"
                + "OS: " + scContactForm.getSenderOperatingSystem() + "\n"
                + "Browser: " + scContactForm.getSenderBrowser() + "\n"
                + "---\n"
                + scContactForm.getSenderMessage();
    }

}
