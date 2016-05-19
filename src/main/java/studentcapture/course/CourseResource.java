package studentcapture.course;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import studentcapture.datalayer.database.ParticipantDAO;

/**
 * CourseResource is a REST controller that maps course related methods to 
 * to REST requests. 
 * 
 * @author tfy12hsm
 *
 */
@RestController
@RequestMapping(value = "/course")
public class CourseResource {
	
	@Autowired
    private CourseDAO courseDAO;
	@Autowired
	private ParticipantDAO participantDAO;
	@Autowired
	private HierarchyDAO hierarchyDAO;

	/**
	 * Adds a course to the database. 
	 * 
	 * @param course
	 * @return
	 */
	@Transactional(rollbackFor=Exception.class)
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "")
    @ResponseBody
    public CourseModel postCourse(
    		@RequestBody
    		CourseModel course) {
		CourseModel result1 = courseDAO.addCourse(course);
    	if(result1.getCourseId()==null) {
    		if(result1.getErrorCode()==HttpStatus.CONFLICT.value())
    			throw new ResourceConflictException();
    		throw new ResourceNotFoundException(); 
    	}
    	if(course.getInitialTeacherId()!=null) {
    	   	Boolean result2 = participantDAO.addParticipant(
    	   			course.getInitialTeacherId().toString(), result1.getCourseId().toString(), 
    	   			"teacher");
    	   	if(!result2) 
    	   		throw new ResourceNotFoundException();
    	}
	   	return result1;
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
    	if(result.getCourseId()==null) {
    		if(result.getErrorCode()==HttpStatus.CONFLICT.value())
    			throw new ResourceConflictException();
    		throw new ResourceNotFoundException(); 
    	}
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
    		@PathVariable(value = "CourseId") Integer courseID) {
    	CourseModel result = courseDAO.getCourse(courseID);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    }
    
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.PUT,
    value = "/{CourseId}")
    @ResponseBody
    public CourseModel putCourseWithId(
    		@PathVariable(value = "CourseId") Integer courseID,
    		@RequestBody CourseModel course) {
    	course.setCourseId(courseID);
    	CourseModel result = courseDAO.updateCourse(course);
    	if(result.getCourseId()==null) {
    		if(result.getErrorCode()==HttpStatus.CONFLICT.value())
    			throw new ResourceConflictException();
    		throw new ResourceNotFoundException(); 
    	}
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
    		@PathVariable(value = "CourseId") Integer courseID) {
    	CourseModel result = courseDAO.removeCourse(courseID);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    } 
    
    /**
     * Returns {@link HierarchyModel} including data related to a given user.
     * 
     * @author tfy12hsm
     * 
     * @param userID	given users identification
     * @return			hierarchy
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "")
    @ResponseBody
    public HierarchyModel getHierarchy(
    		@RequestParam(value="userID") Integer userID) {
    	Optional<HierarchyModel> hierarchy = 
    			hierarchyDAO.getCourseAssignmentHierarchy(userID);
    	if(hierarchy.isPresent()) 
    		return hierarchy.get();
    	throw new ResourceNotFoundException();
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public static class ResourceConflictException extends RuntimeException {
    }
}
