package studentcapture.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
public class SubmissionResource {
    @Autowired
    SubmissionDAO DAO;

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
        return new ResponseEntity<>(DAO.getAllSubmissions(assignmentID), HttpStatus.OK);
    }

    /**
     * Edits a submission, is used to set the grade when the teacher marks the assignment.
     * @param assignmentID
     * @param studentID
     * @param submission
     * @return
     */
    @RequestMapping(value = "{studentID}", method = RequestMethod.PATCH)
    public HttpStatus markSubmission(@PathVariable("assignmentID") int assignmentID,
                                     @PathVariable("studentID") int studentID,
                                     @RequestBody Submission submission){
        submission.setAssignmentID(assignmentID);
        submission.setStudentID(studentID);
        if (DAO.patchSubmission(submission)) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
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
    public HttpStatus storeSubmission(@PathVariable("assignmentID") int assignmentID,
                                      @PathVariable("studentID") int studentID,
                                      @RequestPart(value = "studentVideo") MultipartFile studentVideo,
                                      @RequestPart(value = "submission") Submission updatedSubmission){

        HttpStatus returnStatus;

        updatedSubmission.setStudentID(studentID);
        updatedSubmission.setAssignmentID(assignmentID);
        updatedSubmission.setStudentVideo(studentVideo);
        returnStatus = DAO.addSubmission(updatedSubmission, true) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;

        /*Validation of Submission
        * Should be sent by a student, might have to validate that the student didnt set the grade himself.
        * However this should probably be handled somewhere else
        * validate the Submission.studentID against studentID and permissions*/

        return returnStatus;
    }

    @RequestMapping(value = "{studentID}", method = RequestMethod.DELETE)
    public HttpStatus deleteSubmission(@PathVariable("assignmentID") String assignment,
                                       @PathVariable("studentID") String studentID){
        /*Check permission*/
        return HttpStatus.NOT_IMPLEMENTED;
    }
}
