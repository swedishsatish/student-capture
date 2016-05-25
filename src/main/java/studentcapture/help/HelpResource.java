package studentcapture.help;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import studentcapture.mail.MailClient;

/**
 * Created by g8 on 2016-05-24.
 *
 */
@RestController
@RequestMapping(value = "/help")
public class HelpResource {


    private final MailClient mailClient = new MailClient();

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
        return "getFAQ";
    }

    /**
     *
     */
    @CrossOrigin
    @RequestMapping(
            value = "/form",
            method = RequestMethod.PUT
    )
    public void mailContactForm(@RequestBody SCContactForm scContactForm) {
        String to = "c13bbd@cs.umu.se";
        String subject = "Contact Form";

        // temp
        String message = ""
                + scContactForm.getSenderOperatingSystem() + "\n"
                + scContactForm.getSenderBrowser() + "\n"
                + "---\n"
                + scContactForm.getSenderMessage();

        // yolo
        mailClient.send(to, scContactForm.getSenderEmail(), subject, message);

        System.out.println(message);
    }

}
