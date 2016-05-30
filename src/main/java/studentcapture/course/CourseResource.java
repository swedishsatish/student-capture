package studentcapture.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import studentcapture.course.hierarchy.HierarchyDAO;
import studentcapture.course.hierarchy.HierarchyModel;
import studentcapture.course.participant.Participant;
import studentcapture.course.participant.ParticipantDAO;
import studentcapture.user.User;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * CourseResource is a REST controller that maps course related methods to 
 * to REST requests. 
 * 
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
    		HttpSession session,
    		@RequestBody
    		CourseModel course) {
		if(!participantDAO.hasTeacherPermission(
    			User.getSessionUserId(session)))
    		throw new ResourceUnauthorizedException();
		CourseModel result1 = courseDAO.addCourse(course);
    	if(result1.getCourseId()==null) {
    		if(result1.getErrorCode()==HttpStatus.CONFLICT.value())
    			throw new ResourceConflictException();
    		throw new ResourceNotFoundException(); 
    	}
    	if(course.getInitialTeacherId()!=null) {
			Participant p = new Participant(course.getInitialTeacherId(),result1.getCourseId(),"teacher");
    	   	Boolean result2 = participantDAO.addParticipant(p);
    	   	if(!result2) 
    	   		throw new ResourceNotFoundException();
    	} else {
    		Participant p = new Participant(User.getSessionUserId(session),result1.getCourseId(),"teacher");
    	   	Boolean result2 = participantDAO.addParticipant(p);
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
    		HttpSession session,
    		@RequestBody CourseModel course) {
    	if(!participantDAO.isTeacherOnCourse(
    			User.getSessionUserId(session),
    			course.getCourseId()))
    		throw new ResourceUnauthorizedException();
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
    		HttpSession session,
    		@RequestBody CourseModel course) {
    	if(!participantDAO.isTeacherOnCourse(
    			User.getSessionUserId(session),
    			course.getCourseId()))
    		throw new ResourceUnauthorizedException();
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
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/{CourseId}")
    @ResponseBody
    public CourseModel getCourse(
    		HttpSession session,
    		@PathVariable(value = "CourseId") Integer courseID) {
    	if(!participantDAO.isParticipantOnCourse(
    			User.getSessionUserId(session),
    			courseID))
    		throw new ResourceUnauthorizedException();
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
    		HttpSession session,
    		@PathVariable(value = "CourseId") Integer courseID,
    		@RequestBody CourseModel course) {
    	if(!participantDAO.isTeacherOnCourse(
    			User.getSessionUserId(session),
    			courseID))
    		throw new ResourceUnauthorizedException();
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
    		HttpSession session,
    		@PathVariable(value = "CourseId") Integer courseID) {
    	if(!participantDAO.isTeacherOnCourse(
    			User.getSessionUserId(session),
    			courseID))
    		throw new ResourceUnauthorizedException();
    	CourseModel result = courseDAO.removeCourse(courseID);
    	if(result.getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    } 
    
    /**
     * Returns {@link HierarchyModel} including data related to a given user.
     *
     * given users identification
     * @return			hierarchy
     */
    @CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "")
    @ResponseBody
    public HierarchyModel getHierarchy(HttpSession session) {
    	Optional<HierarchyModel> hierarchy = 
    			hierarchyDAO.getCourseAssignmentHierarchy(User.getSessionUserId(session));
    	if(hierarchy.isPresent()) 
    		return hierarchy.get();
    	throw new ResourceNotFoundException();
    }
    
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 5132715904141343691L;
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public static class ResourceConflictException extends RuntimeException {
		private static final long serialVersionUID = 1L;
    }
    
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public static class ResourceUnauthorizedException extends RuntimeException {
		private static final long serialVersionUID = 1L;
    }
}
