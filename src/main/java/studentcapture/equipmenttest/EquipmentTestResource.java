package studentcapture.equipmenttest;

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

    class TEST{
        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public MultipartFile getVideo() {
            return video;
        }

        public void setVideo(MultipartFile video) {
            this.video = video;
        }

        private String videoName;
        private MultipartFile video;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        private String text;



    }

    /**
     * Necessary for mapping to object TEST.
     * @return
     */
    @ModelAttribute("tst")
    public TEST getTEST(){
        return new TEST();
    }
   
    @CrossOrigin()
    @RequestMapping(value="", method = RequestMethod.POST,
            headers = "content-type=multipart/form-data", produces = "video/webm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity equipmentTest(
            //map inparams to mathing params in TEST. Also take video name.
            @ModelAttribute("tst") TEST test,@RequestParam("videoName") String vName
    ) {
        System.out.println(test.getText() + " " + vName+ " " + test.getVideoName());



        ResponseEntity<String> responseEntity;
        MultipartFile video = test.getVideo();
        String videoName = test.getVideoName();
        if (!video.isEmpty()) {
            try {
                byte[] videoArray = video.getBytes();

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("content-disposition", "inline; filename="+videoName);

                responseEntity = new ResponseEntity<>(Base64.getEncoder().
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
