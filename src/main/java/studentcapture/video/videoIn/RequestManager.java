package studentcapture.video.videoIn;

import org.springframework.core.ExceptionDepthComparator;
import org.springframework.web.bind.annotation.*;

import java.net.URL;

/**
 * Created by leo on 2016-04-26.
 */
@RestController
public class RequestManager {

    /**
     * Request the url to upload a video to, given some "metadata".
     *
     * @param userID The users ID.
     * @param courseID The courses ID.
     * @param examID The exams ID.
     * @return The url to upload a video to.
     * @throws Exception Throws an exception if the user is not a valid user.
     */
    @CrossOrigin
    @RequestMapping(value="/video/inrequest", method = RequestMethod.GET)
    public String requestUpload(
            @RequestParam("userID") String userID,
            @RequestParam("courseID") String courseID,
            @RequestParam("examID") String examID
    ) throws Exception {

        if (!validUser(userID, courseID, examID)) {
            throw new Exception("Not a valid user.");
        }

        String uploadURL = "/video/upload/" + "RANDOM_UNIQUE_STRING";

        // TODO
        createVidInfo();

        return uploadURL;
    }

    /**
     * Checks that a user is valid to upload a video.
     * @return
     */
    private boolean validUser(String userID, String courseID, String examID) {
        return ((userID == "user") && (courseID == "5DV151") && (examID == "1337"));
    }

    /**
     * Creates vidinfo object for fetching
     */
    private void createVidInfo(){
        //TODO give videoqueue information about a new upload!!
    }
}
