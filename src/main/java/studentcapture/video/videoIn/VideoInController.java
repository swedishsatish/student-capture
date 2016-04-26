package studentcapture.video.videoIn;

import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;

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
    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> handleVideoUpload(@RequestParam("userID") String userID,
                                                    @RequestParam("videoName") String videoName,
                                                    @RequestParam("video") MultipartFile video) {
        if (!video.isEmpty()) {
            try {
                /// Send to db and fs.

                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(
                                new File(StudentCaptureApplication.ROOT + "/" + videoName)));


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
        return new ResponseEntity<String>(HttpStatus.OK);
    }

}
