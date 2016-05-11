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
 * @see FeedbackModel
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


    @Autowired
    private RestTemplate requestSender;

    @CrossOrigin
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public HashMap handleFeedbackRequestFromStudent(@Valid FeedbackModel model) {
        //TODO Unsafe data needs to be cleaned
        URI targetURI = constructURI(model, dataLayerHostURI);

        HashMap<String, String> response;
        try {
            response = requestSender.getForObject(targetURI, HashMap.class);
        } catch (RestClientException e) {
            //TODO Maybe not good to send exceptions to browser?
            response = new HashMap<String, String>();
            response.put("error", e.getMessage());
        }
        return response;
    }

    private URI constructURI(@Valid FeedbackModel model, String baseURI) {
        return UriComponentsBuilder.fromUriString(baseURI)
                    .path(dataLayerGetGrade)
                    .queryParam("studentID", model.getStudentID())
                    .queryParam("assignmentID", model.getAssignmentID())
                    .queryParam("courseID", model.getCourseID())
                    .queryParam("courseCode", model.getCourseCode())
                    .build()
                    .toUri();
    }

    @CrossOrigin
    @RequestMapping(value = "video", method = RequestMethod.GET)
    public ResponseEntity<byte[]> video(@Valid FeedbackModel model) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.ALL));
        HttpEntity<String> entity = new HttpEntity(headers);
        ResponseEntity<byte[]> response = null;

        URI targetUrl = constructURI(model, dataLayerHostURI + "/DB/getFeedbackVideo");

        try {
            response = requestSender.exchange(targetUrl, HttpMethod.GET, entity, byte[].class);
        } catch (RestClientException e) {
            //TODO Error handling
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
    public HashMap<String, String> setFeedback(@RequestBody FeedbackModel fm) {

        URI targetUrl = UriComponentsBuilder.fromUriString(dataLayerHostURI)
                .path(dataLayerSetGrade)
                .queryParam("assID", String.valueOf(fm.getAssignmentID()))
                .queryParam("teacherID", fm.getTeacherID())
                .queryParam("studentID", String.valueOf(fm.getStudentID()))
                .queryParam("grade", fm.getGrade())
                .build()
                .toUri();
        HashMap<String, String> addToDB = getExternalResponse(targetUrl);
        if (addToDB.containsKey("error")) {
            return addToDB;
        }


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

        return addToDB;

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
