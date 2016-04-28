package studentcapture.video.videoIn;

import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.config.StudentCaptureApplication;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


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
            @RequestParam("video") MultipartFile video) {


        // Check if url{id} is generated correctly, first done in Request-Manager
        String temp = HashCodeGenerator.generateHash(userID);
        if (!temp.equals(id)) {
            System.err.println("No request done.");
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        } else if (!videoType.equals("question") && !videoType.equals("answer") && !videoType.equals("feedback")) {
            System.err.println("Wrong video type. Videotype: " + videoType);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        String request = "localhost:8181/DB/" + videoType;

        String urlParameters = "userID=" + userID + "&videoName=" + videoName + "&video=" + video;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;

        URL url = null;

        HttpURLConnection conn = null;
        try {
            url = new URL(request);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
            wr.write(postData);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // TODO: Calle & Co: store info to DB and store video in FS

        /*if (!video.isEmpty()) {
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
        }*/

        return new ResponseEntity<String>(HttpStatus.OK);
    }


}
