package studentcapture.datalayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.assignment.AssignmentDAO;
import studentcapture.course.CourseDAO;
import studentcapture.course.HierarchyDAO;
import studentcapture.datalayer.database.ParticipantDAO;
import studentcapture.datalayer.filesystem.FilesystemConstants;
import studentcapture.datalayer.filesystem.FilesystemInterface;
import studentcapture.model.Participant;
import studentcapture.submission.Submission;
import studentcapture.submission.SubmissionDAO;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by c12osn on 2016-04-22.
 * Edited by c13arm, ens13ahr, tfy12hsm
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
    private ParticipantDAO participantDAO;
    @Autowired
    private HierarchyDAO hierarchyDAO;

    //@Autowired
    FilesystemInterface fsi;

    /**
     * Gets the feedback, the actual grade, the grader and the time for a graded submission from the
     * database and the file system.
     *
     * @param submission
     * @return
     */
    /*
    @CrossOrigin()
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "getGrade", method = RequestMethod.GET)
    public Map<String, Object> getGrade(@Valid Submission submission) {
        Map result = submissionDAO.getGrade(submission);
        submission.setCourseCode(courseDAO.getCourseCodeFromId(submission.getCourseID()));
        result.put("feedback", fsi.getFeedbackText(submission));
        return result;
    }
*/

    /**
     *
     * @return
     */
    /*
    @CrossOrigin
    @RequestMapping(value = "/createAssignment", method = RequestMethod.POST)
    public String createAssignment(@RequestBody AssignmentModel assignmentModel)
            throws IllegalArgumentException, IOException {
        Integer assignmentID;
        String courseCode;

        return null;
    }
    */
    /**
     * Upload an assignment video via the file system interface.
     *
     * @param courseCode The course code.
     * @param courseID The course id.
     * @param assignmentID The assignment id, generated by the database when
     *                     the assignment metadata (settings) has been stored.
     * @return The string "OK" if ok, error message otherwise.
     * @author c13ljn
     */
    /*@CrossOrigin
    @RequestMapping(value = "/createAssignmentVideo/{courseCode}/{courseID}/{assignmentID}", method = RequestMethod.POST)
    public String createAssignmentVideo(@PathVariable(value = "courseCode") String courseCode,
                                        @PathVariable(value = "courseID") String courseID,
                                        @PathVariable(value = "assignmentID") String assignmentID,
                                        @RequestParam(value = "video") MultipartFile video) {

        if (FilesystemInterface.storeAssignmentVideo(courseCode, courseID, assignmentID, video)) {
            return "OK";
        } else {
            return "Failed to add assignment video to filesystem.";
        }
    }*/

    /**
     * Save grade for a submission
     *
     * @param submission An object representing a submission
     * @return True if the grade was successfully saved to the database, else false
     */
    @CrossOrigin
    @RequestMapping(value = "/setGrade", method = RequestMethod.POST)
    public boolean setGrade(@RequestBody Submission submission) {
        return submissionDAO.setGrade(submission);
    }

    /**
     * Set feedback for a submission, video and text cannot be null
     * @param submission An object representing a submission
     * @param feedbackVideo Video feedback
     * @param feedbackText Text feedback
     * @return True if feedback was successfully saved to the database, else false
     */
    /*
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
*/
    /**
     * Publish feedback to the student
     * @param submission An object representing a submission
     * @param publish A boolean, true represent publish and false unpublish
     * @return True if feedback could be published/unpublished, else false
     */
    /*@CrossOrigin
    @RequestMapping(value = "/publishFeedback", method = RequestMethod.POST)
    public boolean publishFeedback(@RequestParam(value = "Submission") Submission submission,
                               @RequestParam(value = "Publish") boolean publish) {
        String courseID = assignment.getCourseIDForAssignment(submission.getAssignmentID());
        submission.setCourseID(courseID);
        return submissionDAO.publishFeedback(submission, publish);
    }*/


    /**
     * Fetches information about an assignment.
     * Description is mocked at the moment due to filesystem issues.
     * @param assID Unique identifier for the assignment
     * @return Array containing [course ID, assignment title, opening datetime, closing datetime, minimum video time, maximum video time, description]
     */
//    @CrossOrigin
//    @RequestMapping(value = "/getAssignmentInfo", method = RequestMethod.POST)
//    public Assignment getAssignmentInfo(@RequestParam(value = "assID") int assID){
//
//        Assignment results = assignment.getAssignmentInfo(assID);
//
//        //Need the courseCode for the path
//        //code for the filesystem
//        /*String courseCode = courseDAO.getCourseCodeFromId(results.getCourseID());
//        FileInputStream descriptionStream = fsi.getAssignmentDescription(courseCode, results.getCourseID(), assID);
//        Scanner scanner = new Scanner(descriptionStream);
//        String description = "";
//
//        //Construct description string
//        while (scanner.hasNext()){
//            description += scanner.nextLine() + "\n";
//        }*/
//
//        String description = "beskrivning";
//
//        results.setDescription(description);
//        return results;
//    }

    /**
     * Adds participant to course in database.
     *
     * @param userID		users identifier
     * @param courseID		courses identifier
     * @param function		users function/role in the course
     * @return				true if successful, else false
     *
     * @author tfy12hsm
     */
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
    
    /**
     * Returns function/role of a participant in a course.
     *
     * @param userID		users identifier
     * @param courseID		courses identifier
     * @return				function/role as string, null if no function,role found
     *
     * @author tfy12hsm
     */
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
    
    /**
     *
     *
     * @param userID
     * @return
     *
     * @author tfy12hsm
     */
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
     * Returns list of all ungraded submissions made in response to a given
     * assignment.
     *
     * @param assignmentID		assignment identifier
     * @return					list of submissions
     *
     * @author tfy12hsm
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getAllUngradedSubmissions")
    @ResponseBody
    public List<Submission> getAllUngradedSubmissions(
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
     *
     * @author tfy12hsm
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/getAllSubmissionsWithStudents")
    @ResponseBody
    public List<Submission> getAllSubmissionsWithStudents(
    		@RequestParam(value="assignmentID") String assignmentID) {
    	return submissionDAO.getAllSubmissionsWithStudents(assignmentID).get();
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
    /*@CrossOrigin
    @RequestMapping(value = "/addSubmission/{courseCode}/{courseID}/{assignmentID}/{userID}", method = RequestMethod.POST)
    public String addSubmission(@PathVariable(value = "courseCode") String courseCode,
                                @PathVariable(value = "courseID") String courseID,
                                @PathVariable(value = "assignmentID") String assignmentID,
                                @PathVariable(value = "userID") String userID,
                                @RequestParam(value = "studentConsent") Boolean studentConsent,
                                @RequestParam(value = "video",required = false) MultipartFile video) {
    	if (video == null){
    		if(submissionDAO.addSubmission(, assignmentID, studentConsent)){
    			return "Student submitted an empty answer";
    		}
    		else{
    			return "DB failure for student submission";
    		}
    	}

        // ADD to database here
    	if (submissionDAO.addSubmission(, assignmentID, studentConsent)){
	        if (FilesystemInterface.storeStudentVideo(courseCode, courseID, assignmentID, userID, video)) {
	            return "OK";
	        } else
	            return "Failed to add video to filesystem.";
    	}

    	return "Student has already submitted an answer.";
    }*/

}
