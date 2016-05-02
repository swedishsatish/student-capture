package studentcapture.feedback;

import com.fasterxml.jackson.annotation.JacksonInject;

import java.net.URI;

import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import studentcapture.assignment.AssignmentModel;
import studentcapture.lti.*;

import javax.validation.Valid;
import java.util.HashMap;

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

        HashMap<String, String> response = new HashMap<String, String>();
        try {
            response = requestSender.getForObject(targetUrl, HashMap.class);
        } catch (RestClientException e) {
            //TODO Maybe not good to send exceptions to browser?
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * Will set the given grade for the Feedback.
     * @param feedbackModel The given feedback that will be inserted/updated.
     * @see LTICommunicator
     * @see FeedbackModel
     */
    @RequestMapping(value = "set", method = RequestMethod.POST)
    public void setFeedback(@RequestBody FeedbackModel feedbackModel) {
        //TODO: Set grade in Student-capture (database)


        //TODO: Set grade in LTI.
        try {
            LTICommunicator.setGrade(feedbackModel);
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
