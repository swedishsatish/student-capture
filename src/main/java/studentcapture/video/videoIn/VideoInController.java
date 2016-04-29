
package studentcapture.video.videoIn;

import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.video.VideoInfo;

import java.security.SecureRandom;
import java.math.BigInteger;

import java.io.*;


@RestController
public class VideoInController {

    //This method is called when the user wants to upload an assignment.
    @CrossOrigin()
    @RequestMapping(value = "/uploadAssignment/{id}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadAssignment(
            @PathVariable("id") String id,
            @RequestParam("courseID") String courseID,
            @RequestParam("title") String title,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("minTime") String minTime,
            @RequestParam("maxTime") String maxTime,
            @RequestParam("published") String published,
            @RequestParam("userID") String userID,
            @RequestParam("video") MultipartFile video) {

        final String uri = "localhost:8181/DB/createAssignment";
        VideoInfo newVid = new VideoInfo(title, startDate, endDate, minTime, maxTime, published, courseID, video);
        return uploadVideo(id, userID, uri, newVid);
    }


    //This method is called when the user wants to upload a submission.
    @CrossOrigin()
    @RequestMapping(value = "/uploadSubmission/{id}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadSubmission(
            @PathVariable("id") String id,
            @RequestParam("courseID") String courseID,
            @RequestParam("assignmentID") String assignmentID,
            @RequestParam("studentID") String studentID,
            @RequestParam("userID") String userID,
            @RequestParam("video") MultipartFile video) {


        String uri = "localhost:8181/DB/addSubmission";
        VideoInfo newVid = new VideoInfo(courseID, assignmentID, studentID, video);
        return uploadVideo(id, userID, uri, newVid);
    }


    //This method is called when the user wants to upload an assignment.
    @CrossOrigin()
    @RequestMapping(value = "/uploadFeedback/{id}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadFeedback(
            @PathVariable("id") String id,
            @RequestParam("courseID") String courseID,
            @RequestParam("assignmentID") String assignmentID,
            @RequestParam("studentID") String studentID,
            @RequestParam("userID") String userID,
            @RequestParam("video") MultipartFile video) {


        String uri = "localhost:8181/DB/setFeedback";
        VideoInfo newVid = new VideoInfo(courseID, assignmentID, studentID, video);
        return uploadVideo(id, userID, uri, newVid);
    }



    public ResponseEntity<String> uploadVideo(String id, String userID, String uri, VideoInfo newVid) {
        // Check if url{id} is generated correctly, first done in Request-Manager
        String temp = HashCodeGenerator.generateHash(userID);
        if (!temp.equals(id)) {
            System.err.println("No request done.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uri, newVid, String.class);
        System.out.println(result);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    private String randomizeFilename(String userID) {
        SecureRandom random = new SecureRandom();
        return "video_" + userID  + "_" + new BigInteger(130, random).toString(32).substring(0, 5)+ ".webm";
    }
}