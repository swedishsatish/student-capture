package studentcapture.course.invite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import studentcapture.course.CourseModel;
import studentcapture.course.participant.ParticipantDAO;
import studentcapture.user.User;

import javax.servlet.http.HttpSession;

/**
 * CourseInviteResource is a controller that maps REST requests related to 
 * 
 *
 */
@RestController
@RequestMapping(value = "/invite")
public class CourseInviteResource {
	@Autowired
    private CourseInviteDAO courseInviteDAO;
	@Autowired
    private ParticipantDAO participantDAO;
	
	@CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "")
    @ResponseBody
    public InviteModel postCourseInvite() {
		InviteModel result = new InviteModel();
		result.setCourse(new CourseModel());
		
		return result;
    }
	
	@CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "")
    @ResponseBody
    public InviteModel postCourseInvite(
    		HttpSession session,
    		@RequestBody InviteModel invite) {
		if(!participantDAO.isTeacherOnCourse(
				User.getSessionUserId(session),
				invite.getCourse().getCourseId()))
			throw new ResourceUnauthorizedException();
		InviteModel result = courseInviteDAO.addCourseInvite(invite.getCourse());
    	if(result.getHex()!=null) {
    		return result;
    	} else {
    		throw new ResourceConflictException(); 
    	}
    }
	
	@CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "/{Hex}")
    @ResponseBody
    public InviteModel joinCourse(
    		HttpSession session,
    		@PathVariable(value = "Hex") String hex) {
    	InviteModel result = courseInviteDAO.joinCourseThroughInvite(
    			User.getSessionUserId(session), hex);
    	if(result.getCourse().getCourseId()==null) {
//    		if(result.getCourse().getErrorCode()==HttpStatus.CONFLICT.value()) {
//    			throw new ResourceConflictException();
//    		} else {
//    			throw new ResourceNotFoundException();
//    		}
    	}
    	return result;
    }
	
	@CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/{CourseId}")
    @ResponseBody
    public InviteModel getCourseInviteThroughHex(
    		HttpSession session,
    		@PathVariable(value = "CourseId") Integer courseId) {
		if(!participantDAO.isTeacherOnCourse(
				User.getSessionUserId(session), courseId))
			throw new ResourceUnauthorizedException();
		CourseModel course = new CourseModel();
		course.setCourseId(courseId);
		InviteModel result = courseInviteDAO.addCourseInvite(course);
    	if(result.getHex()!=null) {
    		return result;
    	} else {
    		throw new ResourceConflictException(); 
    	}
    }

	
//	@CrossOrigin
//    @RequestMapping(
//    produces = MediaType.APPLICATION_JSON_VALUE,
//    method = RequestMethod.GET,
//    value = "/{Hex}")
//    @ResponseBody
//    public InviteModel getCourseInviteThroughHex(
//    		@PathVariable(value = "Hex") InviteModel invite) {
//    	InviteModel result = courseInviteDAO.getCourseThroughInvite(invite.getHex());
//    	if(result.getCourse().getCourseId()==null)
//    		throw new ResourceNotFoundException();
//    	return result;
//    }
	
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
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
