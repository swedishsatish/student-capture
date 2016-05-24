
package studentcapture.video.videoIn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @deprecated Use AssignmentResource/SubmissionResource/FeedbackResource instead of VideoInController.
 */
@RestController
public class VideoInController {

    @Autowired
    private RestTemplate requestSender;

    private final String fileTypeExtension = ".webm";

    /**
     * Will send a video and information to DataLayerCommunicator.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on error.
     * @author c13ljn (Modified to support assignment videos)
     * @deprecated
     */
    @CrossOrigin()
    @RequestMapping(value = "/uploadVideo/{id}",
            method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadVideo(
            @PathVariable("id") String id,
            @RequestParam("userID") String userID,
            @RequestParam("assignmentID") String assignmentID,
            @RequestParam("courseID") String courseID,
            @RequestParam("courseCode") String courseCode,
            @RequestParam("videoType") String videoType,
            @RequestParam("video") MultipartFile video) {

        if(video.isEmpty()) {
            // No video was received.
            System.err.println("POST request to /uploadVideo with empty video.");
            return new ResponseEntity<String>("Empty video.", HttpStatus.BAD_REQUEST);
        }

        if (!videoType.equals("assignment") && !videoType.equals("submission") && !videoType.equals("feedback")) {
            System.err.println("Wrong video type. Videotype: " + videoType);
            return new ResponseEntity<>("Unknown type of upload.",HttpStatus.BAD_REQUEST);
        }

        // Generate path that will be used by the DataLayerCommunicator.
        final String uri;
        if (videoType.equals("assignment")) {
            uri = "https://localhost:8443/DB/createAssignmentVideo/" +
                    courseCode + "/" + courseID + "/" + assignmentID;
        } else {
            uri = "https://localhost:8443/DB/addSubmission/" +
                    courseCode + "/" + courseID + "/" + assignmentID + "/" + userID;
        }

        try {
            final byte[] raw = video.getBytes();

            // Use an AbstractResource container for the bytearray.
            // Need a filename or else it will throw NullPointerException on access
            ByteArrayResource videoResource = new ByteArrayResource(raw) {
                @Override
                public String getFilename() {
                    return "video";
                }
            };

            // Send data as a <String, Object> map so that we can receive with @RequestParam("nameOfValue")
            MultiValueMap<String, Object> requestParts = new LinkedMultiValueMap<>();
            requestParts.add("video", videoResource);

            // Send the submission video as a POST request to DataLayerCommunicator at the generated uri
            String response = requestSender.postForObject(uri, requestParts, String.class);

            if(response == null) {
                System.err.println("Could not save video.");
                return new ResponseEntity<String>("Could not store video.", HttpStatus.BAD_REQUEST);
            } else if(!response.equals("OK")) {
                System.err.println("DataLayerComunicator: "+response);
                return new ResponseEntity<String>(response,  HttpStatus.BAD_REQUEST);
            }
        } catch (RestClientException e) {
            System.err.println("Failed to send submission to DataLayerCommunicator.");
            return new ResponseEntity<>("Server error.",  HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e)  {
            System.err.println("Failed to read submitted video.");
            return new ResponseEntity<>("Corrupted video.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK); // Everything went better than expected :)
    }

    /**
     * Generate a random filename.
     * @param userID
     * @return
     * @deprecated
     */
    private String randomizeFilename(String userID) {
        SecureRandom random = new SecureRandom();
        return "video_" + userID  + "_" + new BigInteger(130, random).toString(32).substring(0, 5);
    }
}