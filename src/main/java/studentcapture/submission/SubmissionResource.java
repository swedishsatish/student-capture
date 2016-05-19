package studentcapture.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.assignment.AssignmentDAO;
import studentcapture.course.CourseDAO;
import studentcapture.course.CourseModel;
import studentcapture.datalayer.filesystem.FilesystemInterface;

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
    AssignmentDAO assignmentDAO;
    CourseDAO courseDAO;

    @RequestMapping(value = "{studentID}", method = RequestMethod.GET)
    public ResponseEntity<Submission> getSpecificSubmission(@PathVariable("assignmentID") int assignmentID,
                                                            @PathVariable("studentID") int studentID){
        //TODO fix unity in DAO API

        Submission body = DAO.getSubmission(assignmentID, studentID).get();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Submission>> getAllSubmissions(@PathVariable("assignmentID") int assignmentID){
        //TODO check permissions
        List<Submission> body = DAO.getAllSubmissions(assignmentID).get();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @RequestMapping(value = "{studentID}", method = RequestMethod.PATCH)
    public HttpStatus markSubmission(@PathVariable("assignmentID") String assignmentID,
                                     @PathVariable("studentID") String studentID,
                                     @RequestBody Submission submission){
        //TODO Not implemented - updates partial information of the submission object, such as setting the grade
        /*Validation of Submission
        * if sent by a student: send to a method which only stores the information a student has permission to change (i.e not grade)
        * if sent by a teacher: send to a method which only stores the information a teacher has permission to change (i.e not the answer but the grade)
        *
        * validate the Submission.studentID against studentID and permissions*/
        return HttpStatus.NOT_IMPLEMENTED;
    }


    @RequestMapping(value = "{studentID}", method = RequestMethod.PUT)
    public HttpStatus storeSubmission(@PathVariable("assignmentID") int assignmentID,
                                      @PathVariable("studentID") int studentID,
                                      @RequestBody Submission updatedSubmission){

        updatedSubmission.setStudentID(studentID);
        updatedSubmission.setAssignmentID(assignmentID);
        //TODO Not implemented - stores a submission in the database

        /*Validation of Submission
        * Should be sent by a student, might have to validate that the student didnt set the grade himself.
        * However this should probably be handled somewhere else
        * validate the Submission.studentID against studentID and permissions*/
        return HttpStatus.NOT_IMPLEMENTED;
    }

    @RequestMapping(value = "{studentID}", method = RequestMethod.DELETE)
    public HttpStatus deleteSubmission(@PathVariable("assignmentID") String assignment,
                                       @PathVariable("studentID") String studentID){
        /*Check permission*/
        return HttpStatus.NOT_IMPLEMENTED;
    }

    /**
     * Publish feedback to the student
     * @param submission An object representing a submission
     * @param publish A boolean, true represent publish and false unpublish
     * @return HttpStatus OK if feedback could be published/unpublished, else HttpStatus bad request
     */
    @CrossOrigin
    @RequestMapping(value = "/publishFeedback", method = RequestMethod.POST)
    public HttpStatus publishFeedback(@RequestParam(value = "Submission") Submission submission,
                                      @RequestParam(value = "Publish") boolean publish) {
        String courseID = assignmentDAO.getCourseIDForAssignment(submission.getAssignmentID());
        submission.setCourseID(courseID);
        boolean returnValue = DAO.publishFeedback(submission, publish);
        if(returnValue)
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;
    }

    /**
    * Set feedback for a submission, video and text cannot be null
    * @param submission An object representing a submission
    * @param feedbackVideo Video feedback
    * @param feedbackText Text feedback
    * @return HttpStatus OK if feedback was successfully saved to the database, else HttpStatus bad request
    */
    @CrossOrigin
    @RequestMapping(value = "/setFeedback", method = RequestMethod.POST)
    public HttpStatus setFeedback(@RequestParam(value = "Submission") Submission submission,
                                  @RequestParam(value = "feedbackVideo") MultipartFile feedbackVideo,
                                  @RequestParam(value = "feedbackText") MultipartFile feedbackText) {
        CourseModel courseModel = new CourseModel();
        FilesystemInterface fsi = new FilesystemInterface();
        String courseID = assignmentDAO.getCourseIDForAssignment(submission.getAssignmentID());
        courseModel.setCourseId(Integer.parseInt(courseID));
        String courseCode = courseDAO.getCourseCodeFromId(courseModel);
        submission.setCourseCode(courseCode);
        submission.setCourseID(courseID);
        int feedback = 0;
    	if(feedbackVideo != null) {
            fsi.storeFeedbackVideo(submission, feedbackVideo);
            feedback++;
        }
        if(feedbackText != null) {
            fsi.storeFeedbackText(submission, feedbackText);
            feedback++;
        }

        if(feedback == 0)
            return HttpStatus.BAD_REQUEST;
        else
            return HttpStatus.OK;
    }

    /**
     * Save grade for a submission
     * @param submission An object representing a submission
     * @return HttpStatus OK if grade was successfully saved to the database, else HttpStatus bad request
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public HttpStatus setGrade(@RequestBody Submission submission) {
        boolean returnValue = DAO.setGrade(submission);
        if(returnValue)
            return HttpStatus.OK;
        else
            return HttpStatus.BAD_REQUEST;
    }
}
