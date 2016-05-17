package studentcapture.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import studentcapture.assignment.AssignmentModel;
import studentcapture.lti.LTICommunicator;
import studentcapture.lti.LTIInvalidGradeException;
import studentcapture.lti.LTINullPointerException;
import studentcapture.lti.LTISignatureException;
import studentcapture.model.Submission;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Will deliver or update information about feedback on a Submission for an
 * Assignment.
 *
 * @author Group4, Group6
 * @see AssignmentModel
 * @see Submission
 * @version 0.3 05/02/16 Ongoing - Fixed db-request on set.
 * @since 0.2, 04/29/16 Fixed model.
 * @since 0.1, 04/28/16 Added "set"-mapping method.
 */

@RestController
@RequestMapping(value = "feedback")
public class FeedbackController {
    private static final String dataLayerHostURI = "https://localhost:8443";
    private static final String dataLayerSetGrade = "DB/setGrade";
    private static final String dataLayerGetGrade = "DB/getGrade";
    private static final String dataLayerGetFeedbackVideo = "DB/getFeedbackVideo";
    private static final String dataLayerPublishFeedback = "DB/publishFeedback";

    @Autowired
    private RestTemplate requestSender;

    /**
     * Get the feedback from the datalayer
     * @param submission model with the params needed for getting the feedback
     * @return grade, feedback, teachername and timestamp in a hashmap
     */
    @CrossOrigin
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public HashMap handleFeedbackRequestFromStudent(@Valid Submission submission) {
        //TODO Unsafe data needs to be cleaned
        URI targetURI = constructURI(submission, dataLayerHostURI + dataLayerGetGrade);

        return getExternalResponse(targetURI);
    }

    private URI constructURI(Submission submission, String baseURI) {
        return UriComponentsBuilder.fromUriString(baseURI)
                    .queryParam("studentID", submission.getStudentID())
                    .queryParam("assignmentID", submission.getAssignmentID())
                    .queryParam("courseID", submission.getCourseID())
                    .build()
                    .toUri();
    }

    /**
     * Get the feedback video
     * @param submission model with the params needed for getting the feedback video
     * @return the feedback video
     */
    @CrossOrigin
    @RequestMapping(value = "video", method = RequestMethod.GET)
    public ResponseEntity<byte[]> video(@Valid Submission submission) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity(headers);
        ResponseEntity<byte[]> response = null;

        URI targetUrl = constructURI(submission, dataLayerHostURI + dataLayerGetFeedbackVideo);

        try {
            response = requestSender.exchange(targetUrl, HttpMethod.GET, entity, byte[].class);
        } catch (RestClientException e) {
            //TODO Error handling
        }
        return response;
    }

    /**
     * Will set the given grade for the Submission.
     * @param submission The given grade that will be inserted/updated.
     * @see LTICommunicator
     * @see Submission
     */
    @RequestMapping(value = "set", method = RequestMethod.POST)
    public HashMap<String, String> setGrade(@RequestBody Submission submission) {
        URI targetUrl = UriComponentsBuilder.fromUriString(dataLayerHostURI)
                                            .path(dataLayerSetGrade)
                                            .build()
                                            .toUri();
        HashMap<String, String> response = new HashMap<>();

        try {
            response.put("status", requestSender.postForObject(targetUrl, submission, String.class));
        } catch (RestClientException e) {
            response.put("status", "false");
        }

        //TODO: Set grade in LTI.
        try {
            LTICommunicator.setGrade(submission);
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

        URI targetUrl2 = UriComponentsBuilder.fromUriString(dataLayerHostURI)
                .path(dataLayerPublishFeedback)
                .build()
                .toUri();

        try {
            response.put("status", requestSender.postForObject(targetUrl2, submission, String.class));
        } catch (RestClientException e) {
            response.put("status", "false");
        }

        return response;
    }


    /**
     * Will call an external HTTP, will save response in HashMap.
     * @param targetUrl The URI that will be called.
     * @return  HashMap of the response.
     */
    public HashMap<String, String> getExternalResponse(URI targetUrl) {
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





}
