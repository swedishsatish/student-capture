package studentcapture.video.videoIn;


import org.springframework.core.io.InputStreamResource;
import org.springframework.util.MultiValueMap;
import org.springframework.util.SystemPropertyUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.video.UserData;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.net.URL;
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

        String uploadURL = "/uploadVideo/" + HashCodeGenerator.generateHash(userID);


        return uploadURL;
    }

    /**
     * Request a post of a testing video.E
     * @return The video.
     */
    @CrossOrigin()
    @RequestMapping(value="/video/posttest", method = RequestMethod.GET)
    public MultipartFile requestPostTestVideo(
            @RequestParam("videoName") String videoName,
            @RequestParam("videoTest")MultipartFile videoTest
    ) {

        if (!videoTest.isEmpty()) {
            try {

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(StudentCaptureApplication.ROOT + "/" + videoName)));

                FileCopyUtils.copy(videoTest.getInputStream(), stream);
                stream.close();
            } catch (Exception e) {
                System.err.println("Failed to upload file.");
                return null;
            }
        } else {
            System.err.println("Bad file.");
            return null;
        }
        return videoTest;
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


    @RequestMapping(value = "/videoDownload/{courseCode}/{courseId}/{assignmentId}",
            method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> getAssignmentVideoURL(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("courseId") String courseId,
            @PathVariable("assignmentId") int assignmentId){

        //TODO check userID with back end.
        //TODO send some request to database to check user and get video URL.

        UserData ud = new UserData();
        ud.setAssignmentID(assignmentId);
        ud.setCourseCode(courseCode);
        ud.setCourseID(courseId);

        RestTemplate rt = new RestTemplate();
        URL url = null;

        try {
            //This URL can be changed and shall be changed during testing of course.
            url = new URL("https://mad-eye:9001/getAssignmentVideo");

            //Test print
            System.out.println(url.toURI().getRawPath());

            /*The response that will be sent back to the user with the stream resource*/
            ResponseEntity<?> re;

            //TODO Add eventual response error handler.

            /*Asking the database module for a stream resource as a
            URL that will be relayed from here to the user.*/
            re = rt.postForObject(url.toURI(), ud, ResponseEntity.class);

            if (re != null){
                return (ResponseEntity<InputStreamResource>)re;
            }
        } catch (Exception e) {
            //TODO handle exception
            e.printStackTrace();
        }
        return null;
    }
}