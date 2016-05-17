package studentcapture.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Class:       SubmissionResource
 * <p>
 * Author:      Erik Moström
 * cs-user:     erikm
 * Date:        5/17/16
 */

@RestController
@RequestMapping(value = "assignments/{assignmentID}/submissions/")
public class SubmissionResource {
    @Autowired
    SubmissionDAO DAO;

    @RequestMapping(value = "{studentID}", method = RequestMethod.GET)
    public Submission getSubmission(@PathVariable("assignmentID") String assignment,
                                              @PathVariable("studentID") String studentID){
        //TODO fix unity in DAO API
        return DAO.getSubmission(Integer.parseInt(assignment), Integer.parseInt(studentID)).get();
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Submission> getAllSubmissions(@PathVariable("assignmentID") String assignment){
        return DAO.getAllSubmissions(assignment).get();
    }
}
