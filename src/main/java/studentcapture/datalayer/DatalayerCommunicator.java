package studentcapture.datalayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.assignment.AssignmentModel;
import studentcapture.datalayer.database.Assignment;
import studentcapture.datalayer.database.Course;
import studentcapture.datalayer.database.Submission;
import studentcapture.datalayer.database.Submission.SubmissionWrapper;
import studentcapture.datalayer.database.User;
import studentcapture.datalayer.database.User.CourseAssignmentHierarchy;
import studentcapture.datalayer.filesystem.FilesystemConstants;
import studentcapture.datalayer.filesystem.FilesystemInterface;
import studentcapture.feedback.FeedbackModel;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by c12osn on 2016-04-22.
 * Edited by c13arm, ens13ahr
 */
@RestController
@RequestMapping(value = "/DB")
public class DatalayerCommunicator {



    @Autowired
    private Submission submission;
    @Autowired
    private Assignment assignment;
    @Autowired
    private Course course;
    @Autowired
    private User user;

    //@Autowired
    FilesystemInterface fsi;
    @CrossOrigin()
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "getGrade", method = RequestMethod.GET)
    public Map<String, Object> getGrade(@Valid FeedbackModel model) {
        Map result = submission.getGrade(model.getStudentID(), model.getAssignmentID());
        result.put("feedback", fsi.getFeedbackText(model));
        return result;
    }


    /**
     *
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
    public String createAssignment(@RequestBody AssignmentModel assignmentModel){
        Integer returnResult;

        try{
            returnResult = assignment.createAssignment(assignmentModel.getCourseID(), assignmentModel.getTitle(),
                    assignmentModel.getStartDate(), assignmentModel.getEndDate(), assignmentModel.getMinTimeSeconds(),
                    assignmentModel.getMaxTimeSeconds(), assignmentModel.getPublished());
        } catch (IllegalArgumentException e) {
            //TODO return smarter error msg
            return e.getMessage();
        }

        return returnResult.toString();
    }

    /**
     * Save grade for a submission
     * @param assID Assignment identification
     * @param teacherID Teacher identification
     * @param studentID Student identification
     * @param grade Grade
     * @param teacherConsent Teacher chooses to publish a student's reply
     * @return  True if the grade was successfully saved to the database, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public boolean setGrade(@RequestParam(value = "assID") String assID,
                            @RequestParam(value = "teacherID") String teacherID,
                            @RequestParam(value = "studentID") String studentID,
                            @RequestParam(value = "grade") String grade,
                            @RequestParam(value = "teacherConsent") Boolean teacherConsent) {

        return submission.setGrade(Integer.parseInt(assID), Integer.parseInt(teacherID), Integer.parseInt(studentID), grade, teacherConsent);
    }

    /**
     * Set feedback for a submission, video and text cannot both be null
     * @param assID Assignment identification
     * @param studentID Student identification
     * @param feedbackVideo Video feedback
     * @param feedbackText Text feedback
     * @return True if feedback was successfully saved to the database, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setFeedback", method = RequestMethod.POST)
    public boolean setFeedback(@RequestParam(value = "assID") String assID,
                               @RequestParam(value = "studentID") String studentID,
                               @RequestParam(value = "feedbackVideo") MultipartFile feedbackVideo,
                               @RequestParam(value = "feedbackText") MultipartFile feedbackText) {


        String courseID = assignment.getCourseIDForAssignment(assID);
        String courseCode = course.getCourseCodeFromId(courseID);
        int feedback = 0;
    	if(feedbackVideo != null) {
            fsi.storeFeedbackVideo(courseCode, courseID, assID, studentID, feedbackVideo);
            feedback++;
        }
        if(feedbackText != null) {
            fsi.storeFeedbackText(courseCode, courseID, assID, studentID, feedbackText);
            feedback++;
        }

        return feedback != 0;
    }

    /**
     * Sends the assignment video file.
     * @param courseCode    Courses 6 character identifier.
     * @param courseId      Courses unique database id.
     * @param assignmentId  Assignments unique database id.
     * @return              The video file vie http.
     */
    @CrossOrigin
    @RequestMapping(value = "/getAssignmentVideo/{courseCode}/{courseId}/{assignmentId}",
            method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> getAssignmentVideo(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("courseId") String courseId,
            @PathVariable("assignmentId") String assignmentId) {

        String path = FilesystemInterface.generatePath(courseCode,courseId,assignmentId)
                + FilesystemConstants.ASSIGNMENT_VIDEO_FILENAME;
        ResponseEntity<InputStreamResource> responseEntity = FilesystemInterface.getVideo(path);

        return responseEntity;
    }

    /**
     * Sends the feedback video file.
     * @param model    Model containing the information needed to get the correct video.
     * @return         The video file vie http.
     */
    @CrossOrigin
    @RequestMapping(value = "/getFeedbackVideo",
            method = RequestMethod.GET, produces = "video/webm")
    public ResponseEntity<InputStreamResource> getAssignmentVideo(@Valid FeedbackModel model) {
        String path = FilesystemInterface.generatePath(
                                            model.getCourseCode(),
                                            model.getCourseID(),
                                            Integer.toString(model.getAssignmentID()),
                                            Integer.toString(model.getStudentID()));

        return FilesystemInterface.getVideo(path + FilesystemConstants.FEEDBACK_VIDEO_FILENAME);
   }

    /**
     * Fetches information about an assignment.
     * Description is mocked at the moment due to filesystem issues.
     * @param assID Unique identifier for the assignment
     * @return Array containing [course ID, assignment title, opening datetime, closing datetime, minimum video time, maximum video time, description]
     */
    @CrossOrigin
    @RequestMapping(value = "/getAssignmentInfo", method = RequestMethod.POST)
    public ArrayList<String> getAssignmentInfo(@RequestParam(value = "assID") int assID){

        ArrayList<String> results = assignment.getAssignmentInfo(assID);

        //Need the courseCode for the path
        //code for the filesystem
        /*String courseCode = course.getCourseCodeFromId(results.get(0));
        FileInputStream descriptionStream = fsi.getAssignmentDescription(courseCode, results.get(0), assID);
        Scanner scanner = new Scanner(descriptionStream);
        String description = "";

        //Construct description string
        while (scanner.hasNext()){
            description += scanner.nextLine() + "\n";
        }*/

        String description = "beskrivning";

        results.add(description);
        return results;
    }


    /**
     * Check if given user name and password exist in database.
     * @param username a unique user name.
     * @param pswd password for the unique username
     * @return true  if correct user password and username is given otherwise false
     */
    @CrossOrigin
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public boolean login(@RequestParam(value = "username") String username,
                         @RequestParam(value = "pswd") String pswd) {
        return   user.userExist(username,pswd);
    }

    /**
     * Register user by given information.
     *
     * @param userName user name for the user to be registerd
     * @param fName    First name
     * @param lName    last name
     * @param pNr      social security number
     * @param pwd      password
     * @return true if registration was successfull else false
     */
    @CrossOrigin
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public boolean registerUser(@RequestParam(value = "userName") String userName,
                                @RequestParam(value = "fName") String fName,
                                @RequestParam(value = "lName") String lName,
                                @RequestParam(value = "pNr") String pNr,
                                @RequestParam(value = "pwd") String pwd) {

        return user.addUser(userName,fName,lName,pNr,pwd);
    }

    /**
     * Returns list of all submissions made in response to a given assignment.
     *
     * @param assignmentID		assignment identifier
     * @return					list of submissions
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getAllSubmissions")
    @ResponseBody
    public List<SubmissionWrapper> getAllSubmissions(
    		@RequestParam(value="assignmentID") String assignmentID) {
    	return submission.getAllSubmissions(assignmentID).get();
    }

    /**
     * Returns list of all ungraded submissions made in response to a given
     * assignment.
     *
     * @param assignmentID		assignment identifier
     * @return					list of submissions
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getAllUngradedSubmissions")
    @ResponseBody
    public List<SubmissionWrapper> getAllUngradedSubmissions(
    		@RequestParam(value="assignmentID") String assignmentID) {
    	return submission.getAllUngraded(assignmentID).get();
    }

    /**
     * Returns list of all submissions made in response to a given assignment,
     * including students that are part of the course but has not yet made a
     * submission.
     *
     * @param assignmentID		assignment identifier
     * @return					list of submissions
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getAllSubmissionsWithStudents")
    @ResponseBody
    public List<SubmissionWrapper> getAllSubmissionsWithStudents(
    		@RequestParam(value="assignmentID") String assignmentID) {
    	return submission.getAllSubmissionsWithStudents(assignmentID).get();
    }
    
    /**
     * Returns list of all submissions made in response to a given assignment,
     * including students that are part of the course but has not yet made a
     * submission.
     *
     * @param assignmentID		assignment identifier
     * @return					list of submissions
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getHierarchy")
    @ResponseBody
    public CourseAssignmentHierarchy getHierarchy(
    		@RequestParam(value="userID") String userID) {
    	Optional<CourseAssignmentHierarchy> hierarchy = 
    			user.getCourseAssignmentHierarchy(userID);
    	if(hierarchy.isPresent()) 
    		return hierarchy.get();
    	return null;
    }

    /**
     * Add a submission to the database and filesystem.
     *
     * @param assignmentID
     * @param courseID
     * @param userID
     * @param video
     * @param studentConsent A student allows his teacher to publish his answer to the other students
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addSubmission/{courseCode}/{courseID}/{assignmentID}/{userID}", method = RequestMethod.POST)
    public String addSubmission(@PathVariable(value = "courseCode") String courseCode,
                                @PathVariable(value = "courseID") String courseID,
                                @PathVariable(value = "assignmentID") String assignmentID,
                                @PathVariable(value = "userID") String userID,
                                @RequestParam(value = "studentConsent") Boolean studentConsent,
                                @RequestParam(value = "video",required = false) MultipartFile video) {
    	if (video == null){
    		if(submission.addSubmission(assignmentID, userID, studentConsent)){
    			return "Student submitted an empty answer";
    		}
    		else{
    			return "DB failure for student submission";
    		}
    	}

        // ADD to database here
    	if (submission.addSubmission(assignmentID, userID, studentConsent)){
	        if (FilesystemInterface.storeStudentVideo(courseCode, courseID, assignmentID, userID, video)) {
	            return "OK";
	        } else
	            return "Failed to add video to filesystem.";
    	}

    	return "Student has already submitted an answer.";
    }

}
