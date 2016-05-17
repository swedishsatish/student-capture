package studentcapture.video.videoIn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * https://github.com/student-capture/student-capture/issues/23
 *
 * "As a student I want to be able to leave a blank answer to a
 * question."
 *
 * Created by group 8 on 2016-05-11.
 */
@RestController
public class WithdrawController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Handle wrong requests with a informative http status code.
     * @return 405 Method Not Allowed.
     */
    @CrossOrigin
    @RequestMapping(
            value = "/emptySubmission",
            method = RequestMethod.GET,
            produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> methodNotAllowed() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Handle requests about adding submissions without a video
     * attached to it and respond with some http status code
     * depending on the outcome.
     *
     * @param courseCode    The course code.
     * @param courseID      The course id.
     * @param assignmentID  The assignment id.
     * @param userID        The user id.
     * @return              200 OK,
     *                      500 Internal Server Error,
     *                      501 Not Implemented (lacks ability to fulfill request),
     *                      TODO: (any other codes that happen automatically ???)
     */
    @CrossOrigin
    @RequestMapping(
            value = "/emptySubmission",
            method = RequestMethod.POST,
            produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> leaveEmptySubmission(
            @RequestParam(value = "courseCode") String courseCode,
            @RequestParam(value = "courseID") String courseID,
            @RequestParam(value = "assignmentID") String assignmentID,
            @RequestParam(value = "userID") String userID) {

        final String url = getAddress(courseCode, courseID, assignmentID, userID);

        try {

            /*
             * POST to the specific url, video is null and we expect a
             * string (if any)
             */
            String response = restTemplate.postForObject(url, null, String.class);
            final String expectedResponse = "Student submitted an empty answer";

            if (response == null) {
                // The server lacks the ability to fulfill the request.
                return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

            } else if (!response.equals(expectedResponse)) {
                // addSubmission failed in back-end.
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            } else {
                // Everything went OK.
                return new ResponseEntity<>(HttpStatus.OK);
            }

        } catch (RestClientException e) {
            // When an exception occurs just return status.
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get the final address to the back-end function
     * (make the above easier to read).
     *
     * @param courseCode    The course code.
     * @param courseID      The course id.
     * @param assignmentID  The assignment id.
     * @param userID        The user id.
     * @return              The final address.
     */
    private String getAddress (String courseCode,
                               String courseID,
                               String assignmentID,
                               String userID) {

        String mainAddress = "https://localhost:8443";
        String dbAddress = "/DB/addSubmission";
        String param = courseCode + "/" + courseID + "/" + assignmentID + "/"
                + userID;
        return mainAddress + dbAddress + param;
    }

}
