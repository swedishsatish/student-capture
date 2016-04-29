
package studentcapture.datalayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.video.VideoInfo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by c12osn on 2016-04-22.
 */
@RestController
@RequestMapping(value = "DB")
public class DatalayerCommunicator {
    @Autowired
//private DatabaseCommunicator dbc;
// Not that into what this stuff do, but
    @CrossOrigin()
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "getGrade", method = RequestMethod.POST)
    public MultiValueMap getGrade(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "course", required = false) String course,
                                  @RequestParam(value = "exam", required = false) String exam) {        // Creates the object that should be returned
        LinkedMultiValueMap<String, String> returnData = new LinkedMultiValueMap<String, String>();        // Do your DB and filesystem calls
        //DatabaseCommunicator dbc = new DatabaseCommunicator();
        //String grade = dbc.returnGrade(1, 2);        // Add what you want to return to the map here
        // EX: returndata.add("nyckel", variabel);
        // EX: returndata.add("grade", grade);        // What is returned to the calling address
        return returnData;
    }

    @CrossOrigin()
    @RequestMapping(value = "/upload/question", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadQuestion(@RequestBody VideoInfo videoToUpload) {
        System.out.println("SUCCESS");
        return uploadVideo(videoToUpload, "");
    }

    @CrossOrigin()
    @RequestMapping(value = "/upload/answer", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadAnswer(@RequestBody VideoInfo videoToUpload) {
        System.out.println("SUCCESS");
        return uploadVideo(videoToUpload, "");
    }

    @CrossOrigin()
    @RequestMapping(value = "/upload/feedback", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadFeedback(@RequestBody VideoInfo videoToUpload) {
        System.out.println("SUCCESS");
        return uploadVideo(videoToUpload, "");
    }

    public ResponseEntity<String> uploadVideo(VideoInfo videoToUpload, String uploadPath) {
        MultipartFile video = videoToUpload.getVideoFile();
        if (!video.isEmpty()) {
            try {
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(uploadPath)));
                FileCopyUtils.copy(video.getInputStream(), stream);
                stream.close();
            } catch (Exception e) {
                System.err.println("Failed to upload file.");
                return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
            }
        } else {
            System.err.println("Bad file.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }
}