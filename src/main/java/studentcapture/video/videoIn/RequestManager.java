package studentcapture.video.videoIn;

import org.springframework.util.MultiValueMap;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


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
            @RequestParam("examID") String examID) {


        // TODO: FIX PLZ
        if (!validUser(userID, courseID, examID)) {
             //   throw new Exception("Request not valid.");
        }
        String uploadURL = "/uploadVideo/"  + hashCodeGenerator(userID);
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
            @RequestParam("examID") String examID) {

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

    // TODO: Make more complex
    protected static String hashCodeGenerator(String userID){
        int hashCode = 13;
        hashCode = 31 * hashCode + (int)userID.charAt(1);
        hashCode = 31 * hashCode + (int)userID.charAt(2);
        hashCode = 31 * hashCode + (int)userID.charAt(3);
        hashCode = 31 * hashCode + (int)userID.charAt(0);
        hashCode = 31 * hashCode + (int)userID.charAt(3);
        hashCode = 31 * hashCode + (int)userID.charAt(2);
        hashCode = 31 * hashCode + (int)userID.charAt(1);

        String temp = Integer.toString(hashCode);

        return temp;
    }

}