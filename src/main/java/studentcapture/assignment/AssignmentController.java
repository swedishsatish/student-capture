package studentcapture.assignment;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by victor on 2016-04-28.
 * Controller for assignments. At the moment only contains the method that newly created assignments should be posted
 * to.
 */
@RestController
public class AssignmentController {

    @RequestMapping(value = "/assignment", method = RequestMethod.POST)
    public String postAssignment(@RequestBody AssignmentModel assignment) throws IOException {
        RestTemplate rt = new RestTemplate();
        String res;
        try {
            URL url = new URL("https://localhost:8443/DB/createAssignment");
            res = rt.postForObject(url.toString(), assignment, String.class);

            try {
                // TODO: Result of Integer.parseInt() is ignored. What should actually be done here?
                Integer.parseInt(res);
            } catch (NumberFormatException e) {
                throw new IOException(res);
            }

            return res;

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL in AssignmentController.assignment");
            System.err.println(e.getMessage());
            return e.getMessage();
        }
    }
}
