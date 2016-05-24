package studentcapture.course.invite;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import studentcapture.course.CourseModel;
import studentcapture.login.LoginDAO;

/**
 * CourseInviteResource is a controller that maps REST requests related to 
 * 
 * @author tfy12hsm
 *
 */
@RestController
@RequestMapping(value = "/invite")
public class CourseInviteResource {
	@Autowired
    private CourseInviteDAO courseInviteDAO;
	
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
    		@RequestBody InviteModel invite) {
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
    			LoginDAO.getUserIdFromSession(session), hex);
    	if(result.getCourse().getCourseId()==null) {
    		if(result.getCourse().getErrorCode()==HttpStatus.CONFLICT.value()) {
    			throw new ResourceConflictException();
    		} else {
    			throw new ResourceNotFoundException();
    		}
    	}
    	return result;
    }
	
	@CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET,
    value = "/{Hex}")
    @ResponseBody
    public InviteModel getCourseInviteThroughHex(
    		@PathVariable(value = "Hex") InviteModel invite) {
    	InviteModel result = courseInviteDAO.getCourseThroughInvite(invite.getHex());
    	if(result.getCourse().getCourseId()==null)
    		throw new ResourceNotFoundException();
    	return result;
    }
	
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 1L;
    }
    
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public static class ResourceConflictException extends RuntimeException {
		private static final long serialVersionUID = 1L;
    }
}
