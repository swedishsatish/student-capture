package studentcapture.datalayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.assignment.AssignmentModel;
import studentcapture.datalayer.database.*;
import studentcapture.datalayer.database.SubmissionDAO.SubmissionWrapper;
import studentcapture.datalayer.filesystem.FilesystemConstants;
import studentcapture.datalayer.filesystem.FilesystemInterface;
import studentcapture.feedback.FeedbackModel;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private AssignmentDAO assignment;
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ParticipantDAO participantDAO;
    @Autowired
    private HierarchyDAO hierarchyDAO;

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

        courseCode = courseDAO.getCourseCodeFromId(assignmentModel.getCourseID());
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
     * @param submission An object representing a submission
     * @param grade An object representing a grade
     * @return True if the grade was successfully saved to the database, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public boolean setGrade(@RequestParam(value = "Submission") Submission submission,
                            @RequestParam(value = "Grade") Grade grade) {
        String courseID = assignment.getCourseIDForAssignment(String.valueOf(submission.getAssignmentID()));
        submission.setCourseID(courseID);
        return submissionDAO.setGrade(submission, grade);
    }

    /**
     * Set feedback for a submission, video and text cannot be null
     * @param submission An object representing a submission
     * @param feedbackVideo Video feedback
     * @param feedbackText Text feedback
     * @return True if feedback was successfully saved to the database, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setFeedback", method = RequestMethod.POST)
    public boolean setFeedback(@RequestParam(value = "Submission") Submission submission,
                               @RequestParam(value = "feedbackVideo") MultipartFile feedbackVideo,
                               @RequestParam(value = "feedbackText") MultipartFile feedbackText) {
        String courseID = assignment.getCourseIDForAssignment(String.valueOf(submission.getAssignmentID()));
        String courseCode = courseDAO.getCourseCodeFromId(courseID);
        int feedback = 0;
    	if(feedbackVideo != null) {
            fsi.storeFeedbackVideo(courseCode, courseID, String.valueOf(submission.getAssignmentID()), String.valueOf(submission.getStudentID()), feedbackVideo);
            feedback++;
        }
        if(feedbackText != null) {
            fsi.storeFeedbackText(courseCode, courseID, String.valueOf(submission.getAssignmentID()), String.valueOf(submission.getStudentID()), feedbackText);
            feedback++;
        }

        return feedback != 0;
    }

    /**
     * Publish feedback to the student
     * @param submission An object representing a submission
     * @param publish A boolean, true represent publish and false unpublish
     * @return True if feedback could be published/unpublished, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/publishFeedback", method = RequestMethod.POST)
    public boolean publishFeedback(@RequestParam(value = "Submission") Submission submission,
                               @RequestParam(value = "Publish") boolean publish) {
        String courseID = assignment.getCourseIDForAssignment(String.valueOf(submission.getAssignmentID()));
        submission.setCourseID(courseID);
        return submissionDAO.publishFeedback(submission, publish);
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
    public Assignment getAssignmentInfo(@RequestParam(value = "assID") int assID){

        Assignment results = assignment.getAssignmentInfo(assID);

        //Need the courseCode for the path
        //code for the filesystem
        /*String courseCode = courseDAO.getCourseCodeFromId(results.getCourseID());
        FileInputStream descriptionStream = fsi.getAssignmentDescription(courseCode, results.getCourseID(), assID);
        Scanner scanner = new Scanner(descriptionStream);
        String description = "";

        //Construct description string
        while (scanner.hasNext()){
            description += scanner.nextLine() + "\n";
        }*/

        String description = "beskrivning";

        results.setDescription(description);
        return results;
    }


    /**
     * @param userName
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/userNameExist", method = RequestMethod.GET)
    public boolean userNameExist(
                  @RequestParam(value = "userName") String userName) {
        return userDAO.userNameExist(userName);
    }

    /**
     * @param email
     * @return true if email exist else false
     */
    @CrossOrigin
    @RequestMapping(value = "/userEmailExist", method = RequestMethod.GET)
    public boolean userEmailExist(@RequestParam(value = "email") String email) {
        return userDAO.emailExist(email);
    }

    /**
     * Register user by given information.
     *
     * @return true if registration was successfull else false
     */
    @CrossOrigin
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public void addUser(@RequestParam(value = "jsonStringUser") String jsonStringUser) {
        ObjectMapper mapper = new ObjectMapper();
        User user = null;
        try {
            user = mapper.readValue(jsonStringUser,User.class);
            userDAO.addUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get hashed password for a given username.
     * @param username username for a user
     * @return hashed password else null.
     */
    @CrossOrigin
    @RequestMapping(value = "/getHpswd", method = RequestMethod.POST)
    public String getUserPswd(@RequestParam(value = "username") String username) {
        return userDAO.getPswd(username);
    }
    
    /**
     * Adds a course to the database.
     * 
     * @param courseID
     * @param courseCode
     * @param year
     * @param term
     * @param courseName
     * @param courseDescription		
     * @param active				
     * @return						true i successful, else false
     * 
     * @see Course
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
    	return courseDAO.addCourse(courseID, courseCode, year, term, courseName,
    			courseDescription, active);
    }
    
    @Transactional(rollbackFor=Exception.class)
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "/addCourseWithTeacher")
    @ResponseBody
    public Boolean addCourseWithTeacher(
    		@RequestParam(value="courseID") String courseID,
    		@RequestParam(value="courseCode") String courseCode, 
    		@RequestParam(value="year") String year,
    		@RequestParam(value="term") String term, 
    		@RequestParam(value="courseName") String courseName, 
    		@RequestParam(value="courseDescription") String courseDescription,
    		@RequestParam(value="active") Boolean active,
    		@RequestParam(value="userID") String userID) {
    	 Boolean result1 = courseDAO.addCourse(courseID, courseCode, year, term, 
    			 courseName, courseDescription, active);
    	 Boolean result2 = participantDAO.addParticipant(userID, courseID, 
    			 "Teacher");
    	 if(!(result1 && result2))
    		 throw new RuntimeException();
    	 return (result1 && result2);
    }
    
    /**
     * Returns a course with given identifier.
     *
     * @param courseID		    course identifier
     * @return					found course
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getCourse")
    @ResponseBody
    public Course getCourse(@RequestParam(value="courseID") String courseID) {
    	return courseDAO.getCourse(courseID);
    }

    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "/addParticipant")
    @ResponseBody
    public Boolean addParticipant(
    		@RequestParam(value="userID") String userID, 
    		@RequestParam(value="courseID") String courseID,
    		@RequestParam(value="function") String function) {
    	return participantDAO.addParticipant(userID, courseID, function);
    }
    
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getFunctionForParticipant")
    @ResponseBody
    public String getFunctionForParticipant(
    		@RequestParam(value="userID") String userID, 
    		@RequestParam(value="courseID") String courseID) {
    	Optional<String> result = participantDAO.getFunctionForParticipant(userID, courseID);
    	if(result.isPresent())
    		return result.get();
    	return null;
    }
    
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getAllCoursesIDsForParticipant")
    @ResponseBody
    public List<Participant> getAllCoursesIDsForParticipant(
    		@RequestParam(value="userID") String userID) {
    	Optional<List<Participant>> result = participantDAO.getAllCoursesIDsForParticipant(userID);
    	if(result.isPresent())
    		return result.get();
    	return null;
    }
    
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getAllParticipantsFromCourse")
    @ResponseBody
    public List<Participant> getAllParticipantsFromCourse( 
    		@RequestParam(value="courseID") String courseID) {
    	Optional<List<Participant>> result = participantDAO.getAllParticipantsFromCourse(courseID);
    	if(result.isPresent())
    		return result.get();
    	return null;
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
     * @param userID		assignment identifier
     * @return					list of submissions
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getHierarchy")
    @ResponseBody
    public Hierarchy getHierarchy(
    		@RequestParam(value="userID") String userID) {
    	Optional<Hierarchy> hierarchy = 
    			hierarchyDAO.getCourseAssignmentHierarchy(userID);
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
