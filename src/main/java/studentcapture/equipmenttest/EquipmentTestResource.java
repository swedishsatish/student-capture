package studentcapture.equipmenttest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

/**
 *
 */
@RestController
@RequestMapping("/equipmenttest")
public class EquipmentTestResource {

    /**
     * Receives a video as a MultipartFile and returns the video
     * in a responseEntity containing a encoded string in base64.
     * Used for hardware testing when a user whats to check if the
     * client can send a video to the system and receive the same
     * video.
     *
     * @param userID String containing the ID of the User(optional)
     * @param video MultipartFile of the video to be send back.
     * @return ResponseEntity containing encoded string in base64.
     */
    @CrossOrigin()
    @RequestMapping(value="", method = RequestMethod.POST,
            headers = "content-type=multipart/form-data", produces = "video/webm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InputStreamResource> equipmentTest(
            @RequestParam(value="userID",required = false) String userID,
            @RequestParam(value = "video", required = false) MultipartFile video
    ) {

        ResponseEntity<InputStreamResource> responseEntity;

        if (!video.isEmpty()) {
            try {
                byte[] videoArray = video.getBytes();

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("content-disposition", "inline; filename="+userID);

                responseEntity = new ResponseEntity(Base64.getEncoder().
                        encodeToString(videoArray), responseHeaders, HttpStatus.OK);
            } catch (Exception e) {
                System.err.println("Failed to upload file.");
                return null;
            }
        } else {
            System.err.println("Bad file.");
            return null;
        }


        return responseEntity;
    }

}
