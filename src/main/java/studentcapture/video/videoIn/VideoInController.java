
package studentcapture.video.videoIn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.assignment.AssignmentModel;

import java.security.SecureRandom;
import java.math.BigInteger;

import java.io.*;


@RestController
public class VideoInController {

    @Autowired
    private RestTemplate requestSender;

    private final String fileTypeExtension = ".webm";

    /**
     * Will send a video and information to DataLayerCommunicator.
     *
     * @return Status 200 if success. Status 400 on bad request. Status 500 on error.
     */
    @CrossOrigin()
    @RequestMapping(value = "/uploadVideo/{id}",
            method = RequestMethod.POST, headers = "content-type=multipart/form-data")
    public ResponseEntity<String> uploadVideo(
            @PathVariable("id") String id,
            @RequestParam("video") MultipartFile video,
            @RequestParam("videoType") String videoType,
            @RequestParam("userID") String userID,
            @RequestParam(value = "assignmentID", required = false) String assignmentID,
            @RequestParam(value = "courseID", required = false) String courseID,
            @RequestParam(value = "feedbackText", required = false) MultipartFile feedbackText,
            @RequestParam(value = "studentID", required = false) String studentID,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "minTime", required = false) Integer minTime,
            @RequestParam(value = "maxTime", required = false) Integer maxTime,
            @RequestParam(value = "published", required = false) Boolean published) {

        if (video.isEmpty()) {
            // No video was received.
            System.err.println("POST request to /uploadVideo with empty video.");
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        }

        // Check if url{id} is generated correctly, first done in Request-Manager
        String temp = HashCodeGenerator.generateHash(userID);
        if (!temp.equals(id)) {
            // User has not been granted permission to upload files.
            System.err.println("User has not been granted permission to upload video.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String tempUri;
        MultiValueMap<String, Object> requestParts = new LinkedMultiValueMap<>();

        // Checks if the parameters required are not null for every case.
        switch (videoType) {
            case "assignment":
                if (isAssignmentAttrNotNull(courseID, title, startDate, endDate, minTime, maxTime, published)) {
                    System.err.println("There is an empty parameter. VideoType: assignment");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                tempUri = "https://localhost:8443/DB/createAssignment";
                requestParts.add("assignmentModel", new AssignmentModel(title, "no info", minTime, maxTime, startDate, endDate, published));

                break;
            case "submission":

                if (isSubmissionAttrNotNull(courseID, assignmentID, studentID)) {
                    System.err.println("There is an empty parameter. VideoType: submission");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                tempUri = "https://localhost:8443/DB/addSubmission/" + courseID + "/" + assignmentID + "/" + userID;
                requestParts.add("courseID", courseID);
                requestParts.add("assignmentID", assignmentID);
                requestParts.add("userID", userID);
                break;
            case "feedback":
                if (isFeedbackAttrNotNull(assignmentID, studentID, feedbackText)) {
                    System.err.println("There is an empty parameter. VideoType: feedback");
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }

                tempUri = "https://localhost:8443/DB/setFeedback";
                requestParts.add("assID", assignmentID);
                requestParts.add("studentID", studentID);
                requestParts.add("feedbackText", feedbackText);
                break;
            default:
                // Request must contain the type of upload.
                System.err.println("Wrong video type. Videotype: " + videoType + ". Must be either assignment," +
                        " submission or feedback");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return sendRequest(tempUri, addVideoToMultiValueMap(userID, video, requestParts));
        } catch (IOException e) {
            System.err.println("Failed to read submitted video.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Generate a random filename.
     *
     * @param userID
     * @return
     */
    private String randomizeFilename(String userID) {
        SecureRandom random = new SecureRandom();
        return "video_" + userID + "_" + new BigInteger(130, random).toString(32).substring(0, 5);
    }

    private boolean isAssignmentAttrNotNull(String courseID, String title, String startDate, String endDate,
                                            Integer minTime, Integer maxTime, Boolean published) {
        return (courseID == null || title == null || startDate == null || endDate == null
                || minTime == 0 || maxTime == 0 || published == null);
    }

    private boolean isSubmissionAttrNotNull(String courseID, String assignmentID, String studentID) {
        return courseID == null || assignmentID == null || studentID == null;
    }

    private boolean isFeedbackAttrNotNull(String assignmentID, String studentID, MultipartFile feedbackText) {
        return (assignmentID == null || studentID == null || feedbackText == null);
    }

    private ResponseEntity<String> sendRequest(String tempUri, MultiValueMap<String, Object> requestParts) {
        final String uri = tempUri;
        try {

            // Send the submission video as a POST request to DataLayerCommunicator at /DB/addSubmission
            ResponseEntity<String> response = requestSender.postForEntity(uri, requestParts, String.class);

            if (response == null) {
                System.err.println("Sending data to DataLayerCommunicator failed.");
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("DataLayerComunicator: " + response.getStatusCode().toString());
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (
                RestClientException e
                )

        {
            System.err.println("Failed to send submission to DataLayerCommunicator.");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        {

        }

        return new ResponseEntity<String>(HttpStatus.OK); // Everything went better than expected :)
    }


    private MultiValueMap addVideoToMultiValueMap(String userID, MultipartFile video, MultiValueMap<String,
            Object> requestPartsOld) throws IOException {
        // Generate path and filename that will be used by the DataLayerCommunicator.
        final String filename = randomizeFilename(userID) + fileTypeExtension;
        byte[] raw = null;

//        try {
            // Get the bytes from the video
            raw = video.getBytes();


            // Use an AbstractResource container for the bytearray.
            // Need a filename or else it will throw NullPointerException on access
            ByteArrayResource videoResource = new ByteArrayResource(raw) {
                @Override
                public String getFilename() {
                    return filename;
                }
            };


            // Send data as a <String, Object> map so that we can receive with @RequestParam("nameOfValue")

            requestPartsOld.add("video", videoResource);
            requestPartsOld.add("filename", filename);


            return requestPartsOld;
        }
    }

//
//package studentcapture.video.videoIn;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//import studentcapture.assignment.AssignmentModel;
//
//import java.security.SecureRandom;
//import java.math.BigInteger;
//
//import java.io.*;
//
//
//@RestController
//public class VideoInController {
//
//    @Autowired
//    private RestTemplate requestSender;
//
//    private final String fileTypeExtension = ".webm";
//
//    /**
//     * Will send a video and information to DataLayerCommunicator.
//     *
//     * @return Status 200 if success. Status 400 on bad request. Status 500 on error.
//     */
//    @CrossOrigin()
//    @RequestMapping(value = "/uploadVideo/{id}",
//            method = RequestMethod.POST, headers = "content-type=multipart/form-data")
//    public ResponseEntity<String> uploadVideo(
//            @PathVariable("id") String id,
//            @RequestParam("video") MultipartFile video,
//            @RequestParam("videoType") String videoType,
//            @RequestParam(value = "assignmentID", required = false) String assignmentID,
//            @RequestParam(value = "userID", required = false) String userID,
//            @RequestParam(value = "courseID", required = false) String courseID,
//            @RequestParam(value = "feedbackText", required = false) MultipartFile feedbackText,
//            @RequestParam(value = "studentID", required = false) String studentID,
//            @RequestParam(value = "title", required = false) String title,
//            @RequestParam(value = "startDate", required = false) String startDate,
//            @RequestParam(value = "endDate", required = false) String endDate,
//            @RequestParam(value = "minTime", required = false) Integer minTime,
//            @RequestParam(value = "maxTime", required = false) Integer maxTime,
//            @RequestParam(value = "published", required = false) Boolean published) {
//
//        if(video.isEmpty()) {
//            // No video was received.
//            System.err.println("POST request to /uploadVideo with empty video.");
//            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
//        }
//
//        // Check if url{id} is generated correctly, first done in Request-Manager
//        String temp = HashCodeGenerator.generateHash(userID);
//        if (!temp.equals(id)) {
//            // User has not been granted permission to upload files.
//            System.err.println("User has not been granted permission to upload video.");
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//
//        String tempUri;
//        MultiValueMap<String, Object> requestParts = new LinkedMultiValueMap<>();
//
//        // Checks if the parameters required are not null for every case.
//        switch (videoType) {
//            case "assignment":
//                if (courseID == null || title == null || startDate == null || endDate == null
//                        || minTime == 0 || maxTime == 0 || published == null) {
//                    System.err.println("There is an empty parameter.");
//                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                }
//                tempUri = "https://localhost:8443/DB/createAssignment";
//                requestParts.add("assignmentModel", new AssignmentModel(title, "no info", minTime, maxTime, startDate, endDate, published));
//                break;
//            case "submission":
//                if (courseID == null || assignmentID == null || studentID == null) {
//                    System.err.println("There is an empty parameter.");
//                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                }
//                tempUri = "https://localhost:8443/DB/addSubmission/" + courseID + "/" + assignmentID + "/" + userID;
//                requestParts.add("courseID", courseID);
//                requestParts.add("assignmentID", assignmentID);
//                requestParts.add("userID", userID);
//                break;
//            case "feedback":
//                if (assignmentID == null || studentID == null || feedbackText == null) {
//                    System.err.println("There is an empty parameter.");
//                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                }
//                tempUri = "https://localhost:8443/DB/setFeedback";
//                requestParts.add("assID", assignmentID);
//                requestParts.add("studentID", studentID);
//                requestParts.add("feedbackText", feedbackText);
//                break;
//            default:
//                // Request must contain the type of upload.
//                System.err.println("Wrong video type. Videotype: " + videoType + ". Must be either assignment," +
//                        " submission or feedback");
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        // Generate path and filename that will be used by the DataLayerCommunicator.
//        final String filename = randomizeFilename(userID) + fileTypeExtension;
//        final String uri = tempUri;
//
//        try {
//            // Get the bytes from the video
//            final byte[] raw = video.getBytes();
//
//            // Use an AbstractResource container for the bytearray.
//            // Need a filename or else it will throw NullPointerException on access
//            ByteArrayResource videoResource = new ByteArrayResource(raw) {
//                @Override
//                public String getFilename() {
//                    return filename;
//                }
//            };
//
//            // Send data as a <String, Object> map so that we can receive with @RequestParam("nameOfValue")
//
//            requestParts.add("video", videoResource);
//            requestParts.add("filename", filename);
//
//            // Send the submission video as a POST request to DataLayerCommunicator at /DB/addSubmission
//            ResponseEntity<String> response = requestSender.postForEntity(uri, requestParts, String.class);
//
//            if(response == null) {
//                System.err.println("Sending data to DataLayerCommunicator failed.");
//                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
//            } else if(response.getStatusCode() != HttpStatus.OK) {
//                System.err.println("DataLayerComunicator: "+response.getStatusCode().toString());
//                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } catch (RestClientException e) {
//            System.err.println("Failed to send submission to DataLayerCommunicator.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (IOException e)  {
//            System.err.println("Failed to read submitted video.");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<String>(HttpStatus.OK); // Everything went better than expected :)
//    }
//
//    /**
//     * Generate a random filename.
//     * @param userID
//     * @return
//     */
//    private String randomizeFilename(String userID) {
//        SecureRandom random = new SecureRandom();
//        return "video_" + userID  + "_" + new BigInteger(130, random).toString(32).substring(0, 5);
//    }
//}