package studentcapture.feedback;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import java.util.HashMap;
import studentcapture.assignment.AssignmentModel;
import studentcapture.datalayer.DatalayerCommunicator;
import studentcapture.lti.*;

/**
 * Will deliver or update information about feedback on a Submission for an
 * Assignment.
 *
 * @author Group4, Group6
 * @see AssignmentModel
 * @see FeedbackModel
 * @version 0.2, 04/29/16
 * @since 0.1, 04/28/16 Added "set"-mapping method.
 */

@RestController
@RequestMapping(value = "feedback")
public class FeedbackController {

    @Autowired
    private RestTemplate requestSender;
    private DatalayerCommunicator dc;

    @CrossOrigin
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public HashMap handleFeedbackRequestFromStudent(@Valid FeedbackModel model) {
        //TODO Unsafe data needs to be cleaned
        URI targetUrl = UriComponentsBuilder.fromUriString("https://localhost:8443")
                .path("DB/getGrade")
                .queryParam("studentID", model.getStudentID())
                .queryParam("assignmentID", model.getAssignmentID())
                .queryParam("courseID", model.getCourseID())
                .build()
                .toUri();

        HashMap<String, String> response;
        try {
            response = requestSender.getForObject(targetUrl, HashMap.class);
        } catch (RestClientException e) {
            //TODO Maybe not good to send exceptions to browser?
            response = new HashMap<String, String>();
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * Will set the given grade for the Feedback.
     * @param fm The given feedback that will be inserted/updated.
     * @see LTICommunicator
     * @see FeedbackModel
     */
    @RequestMapping(value = "set", method = RequestMethod.POST)
    public void setFeedback(@RequestBody FeedbackModel fm) {
        //TODO: Set grade in Student-capture (database)
        dc.setGrade(String.valueOf(fm.getAssignmentID()),
                    fm.getTeacherID(),
                    String.valueOf(fm.getStudentID()),
                    fm.getGrade());


        //TODO: Set grade in LTI.
        try {
            LTICommunicator.setGrade(fm);
        }
        catch (LTIInvalidGradeException e) {
            //TODO: Will need to notify the client.
            e.printStackTrace();
        } catch (LTINullPointerException e) {
            //TODO: Ignore.
            e.printStackTrace();
        } catch (LTISignatureException e) {
            //TODO: Will need to notify system administrator.
            e.printStackTrace();
        }


    }





}
