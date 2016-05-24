package studentcapture.course.invite;

import java.util.Optional;

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

@RestController
@RequestMapping(value = "/invite")
public class CourseInviteResource {
	@Autowired
    private CourseInviteDAO courseInviteDAO;
	
	@CrossOrigin
    @RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST,
    value = "")
    @ResponseBody
    public String postCourseInvite(
    		@RequestBody
    		CourseModel course) {
		Optional<String> hex = courseInviteDAO.addCourseInvite(course);
    	if(hex.isPresent()) {
    		return hex.get();
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
    public CourseModel joinCourse(
    		HttpSession session,
    		@PathVariable(value = "Hex") String hex) {
    	CourseModel result = courseInviteDAO.joinCourseThroughInvite(
    			LoginDAO.getUserIdFromSession(session), hex);
    	if(result.getCourseId()==null) {
    		if(result.getErrorCode()==HttpStatus.CONFLICT.value()) {
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
    public CourseModel getCourseInviteThroughHex(
    		@PathVariable(value = "Hex") String hex) {
    	CourseModel result = courseInviteDAO.getCourseThroughInvite(hex);
    	if(result.getCourseId()==null)
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
