package studentcapture.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
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
     * Gets a video from the file system, can get either the feedback video or the submission video.
     * @param assignmentID the assignment of which the feedback/submission video belongs to
     * @param studentID the student which the feedback/submission video belongs to
     * @param fileName the name of the file requested, can be either feedback or submission
     * @return the video file
     */
    @RequestMapping(value = "{studentID}/videos/{fileName}", method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> getFeedbackVideo(@PathVariable("assignmentID") int assignmentID,
                                                                @PathVariable("studentID") int studentID,
                                                                @PathVariable("fileName") String fileName) {
        if(fileName.equals("feedback") || fileName.equals("submission")){
            return DAO.getVideo(new Submission(studentID, assignmentID), fileName + ".webm");
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Gets all submissions for an assignment
     * @param assignmentID
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Submission>> getAllSubmissions(@PathVariable("assignmentID") int assignmentID){
        return new ResponseEntity<>(DAO.getAllSubmissions(assignmentID), HttpStatus.OK);
    }

    /**
     * Edits a submission, is used to set the grade when the teacher sets feedback of
     * the assignment.
     * @param assignmentID  The AssignmentID
     * @param studentID     The studentID
     * @param submission    The Submission
     * @return  HTTP-POST with status and information in body of JSON.
     */
    @RequestMapping(value = "{studentID}", method = RequestMethod.PATCH)
    public ResponseEntity<HashMap<String, String>> markSubmission(HttpSession session,
                                     @PathVariable("assignmentID") int assignmentID,
                                     @PathVariable("studentID") int studentID,
                                     @RequestBody Submission submission) throws IllegalAccessException {

        HashMap<String, String> response = new HashMap<>();
        HttpStatus httpStatus = OK;
        boolean publishFeedback = true;
        int userId = Integer.parseInt(session.getAttribute("userid").toString());

        /* Check so userID is existing */
        if (userId < 0) {
            response.put("Session", "ERROR: not logged in.");
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, httpStatus);
        }

        /* Set all sessions IDs in Submission */
        submission.getGrade().setTeacherID(userId);
        submission.setAssignmentID(assignmentID);
        submission.setStudentID(studentID);

        /* Try save some of the data for the feedback */
        try {
            if (DAO.patchSubmission(submission)) {
                response.put("Save data", "Success");
            } else {
                response.put("Save data", "Notice: Nothing to save.");
            }
        } catch (DataAccessException e) {
            response.put("Save data", "ERROR: Couldn't save the feedback to database/filesystem.");
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, httpStatus);
        }

        /* Save grade */
        try {
            DAO.setGrade(submission, userId);
            response.put("Save grade", "Success");
        } catch (IllegalAccessException e) {
            response.put("Save grade", "ERROR: "+e.getMessage());
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, httpStatus);
        }

        /* Publish the feedback */
        /* TODO: This is not optional at current state. */
        try {
            DAO.publishFeedback(submission, publishFeedback);
            response.put("Publish feedback", "Success");
        } catch (IllegalAccessException e) {
            response.put("Publish feedback", "ERROR: "+e.getMessage());
            httpStatus = INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(response, httpStatus);
        }

        /* Set LTI grade if LTI-link exists */
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


        response.put("Submission", "OK");
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

    /**
     * This will save the teachers feedback-video (Cause of the MultipartFile it must be
     * POST, it should be PATCH) received as a part of the POST.
     * @param assignmentID          The assignmentID of the video
     * @param studentID             The studentID of submission that will recive feedback-Video.
     * @param courseID              The course of the submission-feedback-video.
     * @param teacherFeedbackVideo  The multipartVideo.
     * @return                      Will only return HTTPStatus if saving went successful or not.
     */
    @RequestMapping(value = "{studentID}/feedbackvideo/", method = RequestMethod.POST)
    public HttpStatus storeFeedback(@PathVariable("assignmentID") int assignmentID,
                                    @PathVariable("studentID") int studentID,
                                    @RequestPart(value = "courseID") Integer courseID,
                                    @RequestPart(value = "feedbackVideo") MultipartFile teacherFeedbackVideo){

        Submission submission = new Submission();
        HttpStatus httpStatus = OK;

        submission.setStudentID(studentID);
        submission.setCourseID(courseID);
        submission.setAssignmentID(assignmentID);
        System.out.println("FAIL FÖRE SETFEEDBACK");

        if (!DAO.setFeedbackVideo(submission, teacherFeedbackVideo)) {
            System.out.println("FAIL I SETFEEDBACKVIDEO");
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return httpStatus;
    }

    @RequestMapping(value = "{studentID}", method = RequestMethod.DELETE)
    public HttpStatus deleteSubmission(@PathVariable("assignmentID") String assignment,
                                       @PathVariable("studentID") String studentID) {
        //Check permission
        return HttpStatus.NOT_IMPLEMENTED;
    }
}
