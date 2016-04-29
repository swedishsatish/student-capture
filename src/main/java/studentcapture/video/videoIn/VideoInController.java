
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
    /**
     * Example method.
     * <p/>
     * Will save video at location StudentCaptureApplication.ROOT and videoName as filename.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on bad values.
     */
    @CrossOrigin()
    @RequestMapping(value = "/uploadVideo/{id}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadVideo(
            @PathVariable("id") String id,
            @RequestParam("userID") String userID,
            @RequestParam("videoName") String videoName,
            @RequestParam("videoType") String videoType,
            @RequestParam("video") MultipartFile video) {        // Check if url{id} is generated correctly, first done in Request-Manager
        String temp = HashCodeGenerator.generateHash(userID);
        if (!temp.equals(id)) {
            System.err.println("No request done.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } else if (!videoType.equals("question") && !videoType.equals("answer") && !videoType.equals("feedback")) {
            System.err.println("Wrong video type. Videotype: " + videoType);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        final String uri = "localhost:8181/DB/" + videoType;
        VideoInfo newVid = new VideoInfo(video, userID, videoName);
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