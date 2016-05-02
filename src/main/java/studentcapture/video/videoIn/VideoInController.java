
package studentcapture.video.videoIn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.math.BigInteger;

import java.io.*;


@RestController
public class VideoInController {

    @Autowired
    private RestTemplate requestSender;

    private final String fileTypeExtension = ".webm";

    /**
     * Will send a video and information to DataLayerCommunicator.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on error.
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
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        }

        // Check if url{id} is generated correctly, first done in Request-Manager
        String temp = HashCodeGenerator.generateHash(userID);
        if (!temp.equals(id)) {
            // User has not been granted permission to upload files.
            System.err.println("User has not been granted permission to upload video.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (!videoType.equals("assignment") && !videoType.equals("submission") && !videoType.equals("feedback")) {
            // Request must contain the type of upload.
            System.err.println("Wrong video type. Videotype: " + videoType);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Generate path and filename that will be used by the DataLayerCommunicator.
        final String filename = randomizeFilename(userID) + fileTypeExtension;
        final String uri = "https://localhost:8443/DB/addSubmission/" +
                courseCode+"/"+courseID+"/"+assignmentID+"/"+userID;

        try {
            // Get the bytes from the video
            final byte[] raw = video.getBytes();

            // Use an AbstractResource container for the bytearray.
            // Need a filename or else it will throw NullPointerException on access
            ByteArrayResource videoResource = new ByteArrayResource(raw) {
                @Override
                public String getFilename() {
                    return filename;
                }
            };

            // Send data as a <String, Object> map so that we can receive with @RequestParam("nameOfValue")
            MultiValueMap<String, Object> requestParts = new LinkedMultiValueMap<>();
            requestParts.add("video", videoResource);
            requestParts.add("filename", filename);

            // Send the submission video as a POST request to DataLayerCommunicator at /DB/addSubmission
            ResponseEntity<String> response = requestSender.postForEntity(uri, requestParts, String.class);

            if(response == null) {
                System.err.println("Sending data to DataLayerCommunicator failed.");
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else if(response.getStatusCode() != HttpStatus.OK) {
                System.err.println("DataLayerComunicator: "+response.getStatusCode().toString());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (RestClientException e) {
            System.err.println("Failed to send submission to DataLayerCommunicator.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e)  {
            System.err.println("Failed to read submitted video.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.OK); // Everything went better than expected :)
    }

    /**
     * Generate a random filename.
     * @param userID
     * @return
     */
    private String randomizeFilename(String userID) {
        SecureRandom random = new SecureRandom();
        return "video_" + userID  + "_" + new BigInteger(130, random).toString(32).substring(0, 5);
    }
}