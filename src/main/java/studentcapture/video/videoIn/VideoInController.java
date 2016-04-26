package studentcapture.video.videoIn;

import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;

import java.io.*;
import java.rmi.server.UID;
import java.util.ArrayList;

@RestController
public class VideoInController {

    //RequestManager reqManager = new RequestManager();

    private static volatile ArrayList<String> idList = new ArrayList<String>();

    /**
     * Example method.
     * <p/>
     * Will save video at location StudentCaptureApplication.ROOT and videoName as filename.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on bad values.
     */
    @CrossOrigin()
    @RequestMapping(value = "/uploadVideo/{id}", method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> handleVideoUpload(
            @PathVariable("id") String id,
            @RequestParam("userID") String userID,
            @RequestParam("videoName") String videoName,
            @RequestParam("video") MultipartFile video) {


        if (!idList.contains(id)) {
            System.err.println("No request done.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        idList.remove(id);


        // Now it's up to Connect and Uploader(Calle & Co)


        if (!video.isEmpty()) {
            try {


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

    @CrossOrigin()
    @RequestMapping(value = "/requestUpload", method = RequestMethod.GET)
    public String requestUpload(
            @RequestParam("userID") String userID,
            @RequestParam("courseID") Long courseID,
            @RequestParam("videoName") String videoName){


        // TODO Verify user


        String id = "UserId=" + userID + "CourseID=" + courseID + "VideoName=" + videoName + "RequestID=" + (new UID()).toString();
        idList.add(id);

        return id;
    }

}
