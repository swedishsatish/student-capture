package studentcapture.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.lti.LTICommunicator;
import studentcapture.lti.LTIInvalidGradeException;
import studentcapture.lti.LTINullPointerException;
import studentcapture.lti.LTISignatureException;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * Class:       SubmissionResource
 * <p>
 * Author:      Erik Moström
 * cs-user:     erikm
 * Date:        5/17/16
 */

@CrossOrigin
@RestController
@RequestMapping(value = "assignments/{assignmentID}/submissions/")
class SubmissionResource {
    @Autowired
    private SubmissionDAO DAO;
    @Autowired
    private HttpSession session;

    /**
     * Gets a student's submission on a particular assignment.
     * @param assignmentID The assignment to which a student has submitted an answer.
     * @param studentID The student's ID that determines which of the submissions should be returned.
     * @return A submission object contained in a HTTP ResponseEntity.
     */
    @RequestMapping(value = "{studentID}", method = RequestMethod.GET)
    public ResponseEntity<Submission> getSpecificSubmission(@PathVariable("assignmentID") int assignmentID,
                                                            @PathVariable("studentID") int studentID){
        Submission body = DAO.getSubmission(assignmentID, studentID).get();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    /**
     * Gets the feedback video for a student
     * @param assignmentID
     * @param studentID
     * @return
     */
    @RequestMapping(value = "{studentID}/video", method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> getFeedbackVideo(@PathVariable("assignmentID") int assignmentID,
                                                                @PathVariable("studentID") int studentID) {
        return DAO.getFeedbackVideo(new Submission(studentID, assignmentID));
    }

    /**
     * Gets all submissions for an assignment
     * @param assignmentID
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Submission>> getAllSubmissions(@PathVariable("assignmentID") int assignmentID){
        return new ResponseEntity<>(DAO.getAllSubmissionsWithStudents(Integer.toString(assignmentID)).get(), HttpStatus.OK);
    }

    /**
     * Edits a submission, is used to set the grade when the teacher marks the assignment.
     * @param assignmentID
     * @param studentID
     * @param submission
     * @return
     */
    @RequestMapping(value = "{studentID}", method = RequestMethod.PATCH)
    public ResponseEntity<Map<String, String>> markSubmission(@PathVariable("assignmentID") int assignmentID,
                                     @PathVariable("studentID") int studentID,
                                     @RequestBody Submission submission){
        //TODO: VIDEO-POST

        submission.setAssignmentID(assignmentID);
        submission.setStudentID(studentID);
        //TODO: Set by inRequest variable
        boolean publishFeedback = true;

        Map<String, String> response = new HashMap<>();
        HttpStatus httpStatus = OK;

        if (DAO.patchSubmission(submission)) {
            response.put("Save data", "Success");
        } else {
            response.put("Save data", "ERROR: Couldn't save the feedback to database/filesystem.");
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, httpStatus);
        }

        try {
            DAO.setGrade(submission);
            response.put("Save grade", "Success");
        } catch (IllegalAccessException e) {
            response.put("Save grade", "ERROR: "+e.getMessage());
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, httpStatus);
        }

        /*if (DAO.setFeedbackVideo(submission, feedbackVideo)) {
            response.put("Save video", "Success");
        } else {
            response.put("Save video", "ERROR: Couldn't save the feedbackVideo to filesystem.");
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity(response, httpStatus);
        }*/

        try {
            DAO.publishFeedback(submission, publishFeedback);
            response.put("Publish feedback", "Success");
        } catch (IllegalAccessException e) {
            response.put("Publish feedback", "ERROR: "+e.getMessage());
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, httpStatus);
        }


        try {
            LTICommunicator.setGrade(submission);
            response.put("LTI", "Success");
        } catch (LTINullPointerException e) {
            response.put("LTI", "No LTI exists for this assignment");
        } catch (LTIInvalidGradeException e) {
            response.put("LTI", "ERROR: Invalid grade: "+e.getMessage());
        } catch (LTISignatureException e) {
            response.put("LTI", "ERROR: "+e.getMessage());
        }

        response.put("Submission", submission.toString());
        return new ResponseEntity<>(response, httpStatus);
    }


    /**
     * Stores a submission
     * @param assignmentID
     * @param studentID
     * @param studentVideo
     * @param updatedSubmission
     * @return
     */
    @RequestMapping(value = "{studentID}", method = RequestMethod.POST)
    public ResponseEntity<String> storeSubmission(@PathVariable("assignmentID") int assignmentID,
                                      @PathVariable("studentID") int studentID,
                                      @RequestPart(value = "studentVideo", required = false) MultipartFile studentVideo,
                                      @RequestPart(value = "submission") Submission updatedSubmission){
        String responseText = "OK";
        HttpStatus returnStatus;
        // TODO User from session
        // TODO check if submission can be submitted (begin/end date)
        updatedSubmission.setStudentID(studentID);
        updatedSubmission.setAssignmentID(assignmentID);
        if(studentVideo != null) {
            updatedSubmission.setStudentVideo(studentVideo);
            updatedSubmission.setStatus("answer");
        } else {
            updatedSubmission.setStatus("blank");
        }
        if(DAO.addSubmission(updatedSubmission, true)) {
            returnStatus = HttpStatus.OK;
        } else {
            returnStatus = HttpStatus.FORBIDDEN;
            responseText = "Student has already submitted an answer.";
        }

        /*Validation of Submission
        * Should be sent by a student, might have to validate that the student didnt set the grade himself.
        * However this should probably be handled somewhere else
        * validate the Submission.studentID against studentID and permissions*/

        return new ResponseEntity<>(responseText, returnStatus);
    }

    @RequestMapping(value = "{studentID}", method = RequestMethod.DELETE)
    public HttpStatus deleteSubmission(@PathVariable("assignmentID") String assignment,
                                       @PathVariable("studentID") String studentID){
        /*Check permission*/
        return HttpStatus.NOT_IMPLEMENTED;
    }
}
