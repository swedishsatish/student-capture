package studentcapture.video.videoIn;

import org.springframework.web.bind.annotation.*;


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
     * @param examID The ID of the exam.
     * @return The URL to upload a video to.
     * @throws Exception Throws an exception if the user is not certified to upload a video.
     */
    @CrossOrigin()
    @RequestMapping(value="/video/inrequest", method = RequestMethod.GET)
    public String requestPOSTVideo(
            @RequestParam("userID") String userID,
            @RequestParam("courseID") String courseID,
            @RequestParam("examID") String examID
    ) throws Exception {

        if (!validUser(userID, courseID, examID)) {
            throw new Exception("Request not valid.");
        }

        String uploadURL = "/uploadVideo/" + HashCodeGenerator.generateHash(userID);

        return uploadURL;
    }

    /**
     * Request a URL to get a specific video.
     * @param userID The ID of the user.
     * @param courseID The ID of the course.
     * @param examID The ID of the exam.
     * @return The URL to a video for the user to see in the browser.
     */
    @CrossOrigin()
    @RequestMapping(value="/video/outrequest", method = RequestMethod.GET)
    public String requestGETVideo(
            @RequestParam("userID") String userID,
            @RequestParam("courseID") String courseID,
            @RequestParam("examID") String examID
    ) {

        //TODO: GETVideo method

        return "";
    }

    /**
     * Checks that a user is valid to upload a video.
     * @return
     */
    private boolean validUser(String userID, String courseID, String examID) {
        // TODO: Check if valid user
        return ((userID == "user") && (courseID == "5DV151") && (examID == "1337"));
    }

}