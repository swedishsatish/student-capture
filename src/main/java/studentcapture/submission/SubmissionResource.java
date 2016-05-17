package studentcapture.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Submission> getSubmission(@PathVariable("assignmentID") String assignment,
                                                    @PathVariable("studentID") String studentID){
        //TODO fix unity in DAO API
        return new ResponseEntity<Submission>(DAO.getSubmission(Integer.parseInt(assignment), Integer.parseInt(studentID)).get(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Submission>> getAllSubmissions(@PathVariable("assignmentID") String assignment){
        return new ResponseEntity<List<Submission>>(DAO.getAllSubmissions(assignment).get(), HttpStatus.OK);
    }

    @RequestMapping(value = "{studentID}", method = RequestMethod.PUT)
    public HttpStatus updateSubmission(@PathVariable("assignmentID") String assignment,
                                       @PathVariable("studentID") String studentID,
                                       @RequestBody Submission updatedSubmission){
        /*Validation of Submission
        * if sent by a student: send to a method which only stores the information a student has permission to change (i.e not grade)
        * if sent by a teacher: send to a method which only stores the information a teacher has permission to change (i.e not the answer but the grade)
        *
        * validate the Submission.studentID against studentID and permissions*/
        return HttpStatus.NOT_IMPLEMENTED;
    }

    @RequestMapping(value = "{studentID}", method = RequestMethod.DELETE)
    public HttpStatus deleteSubmission(@PathVariable("assignmentID") String assignment,
                                       @PathVariable("studentID") String studentID){
        /*Check permission*/
        return HttpStatus.NOT_IMPLEMENTED;
    }
}
