package studentcapture.video.videoIn;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;


/**
 * Created by c13ljn on 2016-04-26.
 */
@RestController
public class RequestManager {

    /**
     * Request a URL to upload a video to.
     *
     * @param userID The ID of the user.
     * @param courseID The ID of the course.
     * @param assignmentID The ID of the exam.
     * @return The URL to upload a video to.
     * @throws Exception Throws an exception if the user is not certified to upload a video.
     */
    @CrossOrigin()
    @RequestMapping(value="/video/inrequest", method = RequestMethod.GET)
    public String requestPOSTVideo(
            @RequestParam("userID") String userID,
            @RequestParam("courseID") String courseID,
            @RequestParam("assignmentID") String assignmentID) {


        // TODO: FIX PLZ
        if (!validUser(userID, courseID, assignmentID)) {
             //   throw new Exception("Request not valid.");
        }

        String uploadURL = HashCodeGenerator.generateHash(userID);


        return uploadURL;
    }

    /**
     * Request a URL to get a specific video.
     * @param userID The ID of the user.
     * @param courseID The ID of the course.
     * @param assignmentID The ID of the assignment.
     * @return The URL to a video for the user to see in the browser.
     */
    @CrossOrigin()
    @RequestMapping(value="/video/outrequest", method = RequestMethod.GET)
    public String requestGETVideo(
            @RequestParam("userID") String userID,
            @RequestParam("courseID") String courseID,
            @RequestParam("examID") String assignmentID) {

        //TODO: GETVideo method

        return "";
    }

    /**
     * Receives a video as a MultipartFile and returns the video
     * in a responseEntity containing a encoded string in base64.
     * Used for hardware testing when a user whats to check if the
     * client can send a video to the system and receive the same
     * video.
     *
     * @param userID String containing the ID of the User(optional)
     * @param video MultipartFile of the video to be send back.
     * @return ResponseEntity containing encoded string in base64.
     */
    @CrossOrigin()
    @RequestMapping(value="/video/textTest", method = RequestMethod.POST,
            headers = "content-type=multipart/form-data", produces = "video/webm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InputStreamResource> requestTestingVideo(
            @RequestParam(value="userID",required = false) String userID,
            @RequestParam(value = "video", required = false) MultipartFile video
    ) {

        ResponseEntity<InputStreamResource> responseEntity = null;

        if (!video.isEmpty()) {
            try {
                byte[] videoArray = video.getBytes();

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("content-disposition", "inline; filename="+userID);

                responseEntity = new ResponseEntity(Base64.getEncoder().
                        encodeToString(videoArray), responseHeaders, HttpStatus.OK);
            } catch (Exception e) {
                System.err.println("Failed to upload file.");
                return null;
            }
        } else {
            System.err.println("Bad file.");
            return null;
        }


        return responseEntity;
    }

    /**
     * Checks that a user is valid to upload a video.
     * @return
     */
    private boolean validUser(String userID, String courseID, String assignmentID) {
        // TODO: Check if valid user
        return ((userID == "user") && (courseID == "5DV151") && (assignmentID == "1337"));
    }




}

