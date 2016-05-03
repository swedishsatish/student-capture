package studentcapture.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import studentcapture.assignment.AssignmentModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by victor on 2016-04-28.
 */
@RestController
public class AssignmentController {

    @RequestMapping(value = "/assignment", method = RequestMethod.POST)
    public AssignmentModel postAssignment(@RequestBody AssignmentModel assignment) throws IOException {
        RestTemplate rt = new RestTemplate();

        try {
            URL url = new URL("https://localhost:8443/DB/createAssignment");
            int res = rt.postForObject(url.toString(), assignment, int.class);

            if(res < 0) {
                throw new IOException("Couldn't access database layer");
            }

        } catch (MalformedURLException e) {
            System.err.println("Malformed URL in AssignmentController.assignment");
            System.err.println(e.getMessage());
        }

        return assignment;
    }
}
