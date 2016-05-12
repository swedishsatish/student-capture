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
import studentcapture.datalayer.database.*;
import studentcapture.datalayer.database.SubmissionDAO.SubmissionWrapper;
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
    private SubmissionDAO submissionDAO;
    @Autowired
    private Assignment assignment;
    @Autowired
    private CourseDAO course;
    @Autowired
    private User user;

    //@Autowired
    FilesystemInterface fsi;
    @CrossOrigin()
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "getGrade", method = RequestMethod.GET)
    public Map<String, Object> getGrade(@Valid FeedbackModel model) {
        Map result = submissionDAO.getGrade(model.getStudentID(), model.getAssignmentID());
        result.put("feedback", fsi.getFeedbackText(model));
        return result;
        //return submissionDAO.getGrade(model.getStudentID(), model.getAssignmentID());
    }


    /**
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/createAssignment", method = RequestMethod.POST)
    public String createAssignment(@RequestBody AssignmentModel assignmentModel)
            throws IllegalArgumentException, IOException {
        Integer assignmentID;
        String courseCode;

        assignmentID = assignment.createAssignment(assignmentModel.getCourseID(), assignmentModel.getTitle(),
                assignmentModel.getStartDate(), assignmentModel.getEndDate(), assignmentModel.getMinTimeSeconds(),
                assignmentModel.getMaxTimeSeconds(), assignmentModel.getPublished());

        courseCode = course.getCourseCodeFromId(assignmentModel.getCourseID());
        FilesystemInterface.storeAssignmentText(courseCode, assignmentModel.getCourseID(),
                assignmentID.toString(), assignmentModel.getInfo(),
                FilesystemConstants.ASSIGNMENT_DESCRIPTION_FILENAME);
        FilesystemInterface.storeAssignmentText(courseCode, assignmentModel.getCourseID(),
                assignmentID.toString(), assignmentModel.getRecap(),
                FilesystemConstants.ASSIGNMENT_RECAP_FILENAME);

        return assignmentID.toString();
    }

    /**
     * Save grade for a submission
     *
     * @param submission Object containing assignmentID, studentID
     * @param grade Object containing grade, teacherID, date, publish
     * @return True if the grade was successfully saved to the database, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public boolean setGrade(@RequestParam(value = "Submission") Submission submission,
                            @RequestParam(value = "Grade") Grade grade) {
        return submissionDAO.setGrade(submission, grade);
    }

    /**
     * Set feedback for a submission, video and text cannot be null
     * @param submission Object containing assignmentID, studentID
     * @param feedbackVideo Video feedback
     * @param feedbackText Text feedback
     * @return True if feedback was successfully saved to the database, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setFeedback", method = RequestMethod.POST)
    public boolean setFeedback(@RequestParam(value = "Submission") Submission submission,
                               @RequestParam(value = "feedbackVideo") MultipartFile feedbackVideo,
                               @RequestParam(value = "feedbackText") MultipartFile feedbackText) {
        String courseID = assignment.getCourseIDForAssignment(submission.getAssignmentID() + "");
        String courseCode = course.getCourseCodeFromId(courseID);
        int feedback = 0;
    	if(feedbackVideo != null) {
            fsi.storeFeedbackVideo(courseCode, courseID, submission.getAssignmentID() + "", submission.getStudentID() + "", feedbackVideo); // submission.getstudentID() should be int/String?
            feedback++;
        }
        if(feedbackText != null) {
            fsi.storeFeedbackText(courseCode, courseID, submission.getAssignmentID() + "", submission.getStudentID() + "", feedbackText); // submission.getstudentID() should be int/String?
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

    //  public void add user
    // public void userExist
    // public void userEmailExist


    /**
     * @param userName
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/userNameExist", method = RequestMethod.GET)
    public boolean userNameExist(
                  @RequestParam(value = "userName") String userName) {
        return false;
    }

    /**
     * @param email
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/usrEmailExist", method = RequestMethod.GET)
    public boolean userEmailExist(@RequestParam(value = "email") String email) {
        return false;
    }

    /**
     * Register user by given information.
     *
     * @param userName user name for the user to be registerd
     * @param fName    First name
     * @param lName    last name
     * @param email
     * @param salt     salt for password
     * @param pwd      password
     * @return true if registration was successfull else false
     */
    @CrossOrigin
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public  void addUser(@RequestParam(value = "userName") String userName,
                                @RequestParam(value = "fName") String fName,
                                @RequestParam(value = "lName") String lName,
                                @RequestParam(value = "email") String email,
                                @RequestParam(value = "salt") String salt,
                                @RequestParam(value = "pwd") String pwd) {

        user.addUser(userName,fName,lName,email,salt,pwd);
    }
	
    /**
     * Adds a course to the database.
     *
     * @param assignmentID		assignment identifier
     * @return					list of submissions
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "/addCourse")
    @ResponseBody
    public Boolean addCourse(
    		@RequestParam(value="courseID") String courseID,
    		@RequestParam(value="courseCode") String courseCode, 
    		@RequestParam(value="year") String year,
    		@RequestParam(value="term") String term, 
    		@RequestParam(value="courseName") String courseName, 
    		@RequestParam(value="courseDescription") String courseDescription,
    		@RequestParam(value="active") Boolean active) {
    	return course.addCourse(courseID, courseCode, year, term, courseName,
    			courseDescription, active);
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
    	return submissionDAO.getAllSubmissions(assignmentID).get();
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
    	return submissionDAO.getAllUngraded(assignmentID).get();
    }

    /**
     * Returns list of all submissions made in response to a given assignment,
     * including students that are part of the course but has not yet made a
     * submissionDAO.
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
    	return submissionDAO.getAllSubmissionsWithStudents(assignmentID).get();
    }
    
    /**
     * Returns list of all submissions made in response to a given assignment,
     * including students that are part of the course but has not yet made a
     * submissionDAO.
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
    		if(submissionDAO.addSubmission(assignmentID, userID, studentConsent)){
    			return "Student submitted an empty answer";
    		}
    		else{
    			return "DB failure for student submission";
    		}
    	}

        // ADD to database here
    	if (submissionDAO.addSubmission(assignmentID, userID, studentConsent)){
	        if (FilesystemInterface.storeStudentVideo(courseCode, courseID, assignmentID, userID, video)) {
	            return "OK";
	        } else
	            return "Failed to add video to filesystem.";
    	}

    	return "Student has already submitted an answer.";
    }

}
