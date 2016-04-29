/* Grupp 5 */

package studentcapture.video.videoOut;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.datalayer.filesystem.FilesystemInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class VideoOutController {

    /**
     * Example method.
     *
     * Precondition: Need video on disk at location StudentCaptureApplication.ROOT/video.webm
     *
     * @return Inlined video to the client if video exists on disk.
     */
    @RequestMapping(value = "/videoDownload", method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> handleVideoUpload(@PathVariable("video") String videoName) {
        String filename = "video.webm";
        String filepath = StudentCaptureApplication.ROOT;
        ResponseEntity responseEntity = null;
        byte []file = null;
        
        File video = new File(filepath+filename);

        if(video.exists()) {
            try {
                byte []out = FileCopyUtils.copyToByteArray(video);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("content-disposition", "inline; filename="+filename);

                responseEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);
            } catch (IOException e) {
                responseEntity = new ResponseEntity("Error getting file", HttpStatus.OK);
            }
        } else {
            responseEntity = new ResponseEntity("File not found", HttpStatus.OK);
        }

        return responseEntity;
    }
    @RequestMapping(value = "/videoDownload/{video}", method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> handleVideDl(@PathVariable("video") String videoName) {
        String filename = "/"+videoName+".webm";

        String filepath = StudentCaptureApplication.ROOT;
        ResponseEntity responseEntity = null;
        byte []file = null;

        File video = new File(filepath+filename);

        if(video.exists()) {
            try {
                byte []out = FileCopyUtils.copyToByteArray(video);
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("content-disposition", "inline; filename="+filename);

                responseEntity = new ResponseEntity(out, responseHeaders, HttpStatus.OK);
            } catch (IOException e) {
                responseEntity = new ResponseEntity("Error getting file", HttpStatus.OK);
            }
        } else {
            responseEntity = new ResponseEntity("File not found", HttpStatus.OK);
        }

        return responseEntity;
    }

    /**
     * Gets a specific video from the FilesystemInterface and returns 
     * it as a byte array.
     * 
     * @param courseCode    courses 6 character identifier
     * @param courseId      courses unique database id
     * @param assignmentId  assignments unique database id
     * @param userId        users unique database id
     * @return              a video byte array
     * @author              Stefan Embretsen
     */
    @RequestMapping(value = "/videoDownload/{courseCode}/{courseId}/{assignmentId}/{userId}",
            method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> handleVideDl(@PathVariable("courseCode") String courseCode,
            @PathVariable("courseId") int courseId, @PathVariable("assignmentId") int assignmentId,
            @PathVariable("userId") int userId) {

        ResponseEntity responseEntity = null;

        FilesystemInterface fis = new FilesystemInterface();
        
        FileInputStream videoIS = fis.getStudentVideo
                (courseCode, courseId, assignmentId, userId);
        
        if(videoIS != null){
            try {
                //Sets byte array to the same size as the video file.
                byte []out = new byte[fis.getVideoFileSize
                                 (courseCode, courseId, assignmentId, userId)];
                //Reads the video file to byte array.
                videoIS.read(out);
                HttpHeaders responseHeaders = new HttpHeaders();
                
                responseHeaders.add
                ("content-disposition", "inline; filename=StudentCaptureVideo");
                
                responseEntity = new ResponseEntity
                        (out, responseHeaders, HttpStatus.OK);
                
            } catch (IOException e) {
                responseEntity = new ResponseEntity
                        ("Error getting file", HttpStatus.OK);
            }
        }else {
            responseEntity = new ResponseEntity("File not found", HttpStatus.OK);
        }
        return responseEntity;
        

    }

}
