/* Grupp 5 */

package studentcapture.video.videoOut;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import studentcapture.config.StudentCaptureApplication;

import java.io.File;
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
    public ResponseEntity<InputStreamResource> handleVideoUpload() {
        String filename = "/video.webm";
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

}
