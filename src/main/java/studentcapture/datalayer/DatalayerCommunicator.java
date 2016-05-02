package studentcapture.datalayer;

import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import studentcapture.datalayer.database.Submission;
import studentcapture.datalayer.filesystem.FilesystemInterface;
import studentcapture.feedback.FeedbackModel;

import javax.validation.Valid;

/**
 * Created by c12osn on 2016-04-22.
 * Edited by c13arm, ens13ahr
 */
@RestController
@RequestMapping(value = "DB")
public class DatalayerCommunicator {


    @Autowired
    private Submission submission;
    //@Autowired
    //private Assignment assignment;

    @CrossOrigin()
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "getGrade", method = RequestMethod.GET)
    public Hashtable<String, Object> getGrade(@Valid FeedbackModel model) {
        return submission.getGrade(model.getStudentID(), model.getAssignmentID());
    }


    /**
     * @param courseID
     * @param assignmentTitle
     * @param startDate
     * @param endDate
     * @param minTime
     * @param maxTime
     * @param published
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/createAssignment", method = RequestMethod.POST)
    public int createAssignment(@RequestParam(value = "courseID") String courseID,
                                @RequestParam(value = "assignmentTitle") String assignmentTitle,
                                @RequestParam(value = "startDate") String startDate,
                                @RequestParam(value = "endDate") String endDate,
                                @RequestParam(value = "minTime") String minTime,
                                @RequestParam(value = "maxTime") String maxTime,
                                @RequestParam(value = "published") boolean published) {

        //int returnResult = ass.createAssignment(courseID, assignmentTitle, startDate, endDate, minTime, maxTime, published);

        return 1234;//returnResult;
    }

    /**
     * Save grade for a submission
     *
     * @param assID
     * @param teacherID
     * @param studentID
     * @param grade
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public boolean setGrade(@RequestParam(value = "assID") String assID,
                            @RequestParam(value = "teacherID") String teacherID,
                            @RequestParam(value = "studentID") String studentID,
                            @RequestParam(value = "grade") String grade) {

        return false;
    }

    /**
     * Give feedback for a submission
     *
     * @param assID
     * @param teacherID
     * @param studentID
     * @param feedbackVideo Can be null
     * @param feedbackText  Can be null
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/giveFeedback", method = RequestMethod.POST)
    public boolean giveFeedback(@RequestParam(value = "assID") String assID,
                                @RequestParam(value = "teacherID") String teacherID,
                                @RequestParam(value = "studentID") String studentID/*,
                            @RequestParam(value = "feedbackVideo") video feedbackVideo,
            				@RequestParam(value = "feedbackText") text feedbackText*/) {

        return false;
    }


    /**
     * Add a submission to the database and filesystem.
     *
     * @param assignmentID
     * @param courseID
     * @param userID
     * @param video
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addSubmission/{courseCode}/{courseID}/{assignmentID}/{userID}", method = RequestMethod.POST)
    public ResponseEntity<String> addSubmission(@PathVariable(value = "courseCode") String courseCode,
                                                @PathVariable(value = "courseID") String courseID,
                                                @PathVariable(value = "assignmentID") String assignmentID,
                                                @PathVariable(value = "userID") String userID,
                                                @RequestParam(value = "filename") String filename,
                                                @RequestParam(value = "video") MultipartFile video) {

        // ADD to database here

        if (FilesystemInterface.storeStudentVideo(courseCode, courseID, assignmentID, userID, video, filename)) {
            return new ResponseEntity<String>(HttpStatus.OK);
        } else
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
