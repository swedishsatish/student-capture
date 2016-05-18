package studentcapture.course;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import studentcapture.datalayer.database.ParticipantDAO;

/**
 * 
 * @author tfy12hsm
 *
 */
@RestController
@RequestMapping(value = "/Course")
public class CourseResource {
	
	@Autowired
    private CourseDAO courseDAO;
	@Autowired
	private ParticipantDAO participantDAO;
	
	/**
	 * Adds a course to the database. 
	 * 
	 * @param course
	 * @return
	 */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "")
    @ResponseBody
    public CourseModel postCourse(
    		@RequestBody CourseModel course) {
    	CourseModel result = courseDAO.addCourse(course);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    }
    
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.PUT,
    value = "")
    @ResponseBody
    public CourseModel putCourse(
    		@RequestBody CourseModel course) {
    	CourseModel result = courseDAO.updateCourse(course);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    }
    
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.DELETE,
    value = "")
    @ResponseBody
    public CourseModel deleteCourse(
    		@RequestBody CourseModel course) {
    	CourseModel result = courseDAO.removeCourse(course);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    }
    
    /**
     * Returns a course with given identifier.
     *
     * @param courseID		    course identifier
     * @return					found course
     *
     * @author tfy12hsm
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/{CourseId}")
    @ResponseBody
    public CourseModel getCourse(
    		@PathVariable(value = "CourseId") String courseID) {
    	CourseModel result = courseDAO.getCourse(courseID);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    }
    
    /**
     * 
     * @param courseID
     * @return
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.DELETE,
    value = "/{CourseId}")
    @ResponseBody
    public CourseModel deleteCourse(
    		@PathVariable(value = "CourseId") String courseID) {
    	CourseModel result = courseDAO.removeCourse(courseID);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    }
    
//    /**
//     * Adds a course with a teacher.
//     * 
//     * @param course
//     * @param teacherID
//     * @return
//     */
//    @Transactional(rollbackFor=Exception.class)
//    @CrossOrigin
//    @RequestMapping(
//    produces = MediaType.APPLICATION_JSON_VALUE,
//    method = RequestMethod.POST,
//    value = "")
//    @ResponseBody
//    public Course postCourseWithTeacher(
//    		@RequestBody Course course,
//    		@RequestParam(value="teacherID") String teacherID) {
//    	 Course result1 = courseDAO.addCourse(course);
//    	 if(result1.getCourseId()==null) 
//    		 throw new ResourceNotFoundException();
//    	 Boolean result2 = participantDAO.addParticipant(teacherID, result1.getCourseId(), 
//    			 "Teacher");
//    	 if(!result2) 
//    		 throw new ResourceNotFoundException();
//    	 return result1;
//    }
    
    
//    /**
//     * Returns list of all submissions made in response to a given assignment,
//     * including students that are part of the course but has not yet made a
//     * submissionDAO.
//     *
//     * @param userID		assignment identifier
//     * @return					list of submissions
//     *
//     * @author tfy12hsm
//     */
//    @CrossOrigin
//    @RequestMapping(
//    produces = MediaType.APPLICATION_JSON_VALUE,
//    method = RequestMethod.GET,
//    value = "/getHierarchy")
//    @ResponseBody
//    public HierarchyModel getHierarchy(
//    		@RequestParam(value="userID") String userID) {
//    	Optional<HierarchyModel> hierarchy = 
//    			hierarchyDAO.getCourseAssignmentHierarchy(userID);
//    	if(hierarchy.isPresent()) 
//    		return hierarchy.get();
//    	return null;
//    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public static class ResourceConflictException extends RuntimeException {
    }
}
