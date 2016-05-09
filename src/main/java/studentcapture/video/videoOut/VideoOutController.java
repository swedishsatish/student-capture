package studentcapture.video.videoOut;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@RestController
@RequestMapping(value = "/video/download")
public class VideoOutController {

    /**
     * Return the assignment video corresponding to the input parameters.
     * @param courseCode    Courses 6 character identifier.
     * @param courseId      Courses unique database id.
     * @param assignmentId  Assignments unique database id.
     * @return              The video in a ResponseEntity.
     */
    @RequestMapping(value = "/assignment/{courseCode}/{courseId}/{assignmentId}",
            method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> getAssignmentVideo(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("courseId") String courseId,
            @PathVariable("assignmentId") int assignmentId){

        try {
            URL url = new URL("https://localhost:8443/DB/getAssignmentVideo/" +
                    courseCode + "/" + courseId + "/"+assignmentId);

            return getVideoFromDB(url.toURI());
        } catch (MalformedURLException e) {
            return new ResponseEntity("Error getting file.", HttpStatus.NOT_FOUND);
        } catch (URISyntaxException e) {
            return new ResponseEntity("Error getting file.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Used to get the video from the database. Uses Http to get the video from DB.
     * Right now the parameters needs to be in the URI and taken care of with
     * @ PathVariable in the DB.
     * @param dbURI The URI to the DB with the parameters.
     * @return The video in a ResponseEntity.
     */
    private ResponseEntity<InputStreamResource> getVideoFromDB(URI dbURI) {
        ResponseEntity<InputStreamResource> responseEntity;
        RestTemplate restTemplate = new RestTemplate();

        //TODO check userID with back end. Authorization?

        try {
            // Extract movie from inputstream.
            ResponseExtractor<byte[]> responseExtractor = new ResponseExtractor<byte[]>() {
                @Override
                public byte[] extractData(ClientHttpResponse clientHttpResponse)
                        throws IOException {
                    return FileCopyUtils.copyToByteArray(clientHttpResponse.getBody());
                }
            };

            RequestCallback requestCallback = new RequestCallback() {
                @Override
                public void doWithRequest(ClientHttpRequest clientHttpRequest)
                        throws IOException {
                    // does nothing just needs to be sent.
                }
            };

            byte[] out = restTemplate.execute(dbURI.toString(),
                    HttpMethod.GET,requestCallback, responseExtractor);
            // Free to do something with the file here. decompress, deencrypt?

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("content-disposition", "inline; filename=Video");
            responseEntity = new ResponseEntity(out,responseHeaders,HttpStatus.OK);

        } catch (RestClientException e) {
            responseEntity = new ResponseEntity("Error getting file.", HttpStatus.NOT_FOUND);
            e.printStackTrace();
        }

        return responseEntity;
    }



}
