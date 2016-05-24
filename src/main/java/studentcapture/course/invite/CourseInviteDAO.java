package studentcapture.course.invite;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.course.CourseDAO;
import studentcapture.course.CourseModel;
import studentcapture.course.participant.ParticipantDAO;

/**
 * 
 * @author tfy12hsm
 *
 */
@Repository
public class CourseInviteDAO {
	private static final long INVITE_DURATION = 1000*60*60*24*30*2; // in milliseconds
	private static final int HEX_LENGTH = 8;
	
	@Autowired
    protected JdbcTemplate jdbcTemplate;
	
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private ParticipantDAO participantDAO;
	
	private static SecureRandom random = new SecureRandom();;
	
	private String generateRandomHexString(int length) {
		byte[] bytes = new byte[length/2];
		StringBuilder sb = new StringBuilder();
		random.nextBytes(bytes);
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}
	
    public InviteModel addCourseInvite(CourseModel course) {
        String addCourseInviteStatement =
                "INSERT INTO CourseInvite VALUES (?,?,?)";
        
        InviteModel previousInvite = getCourseInvite(course);
        if(previousInvite.getHex()!=null) {
        	return previousInvite;
        }
        
        String newHex = generateRandomHexString(HEX_LENGTH);
        try {
            int rowsAffected = jdbcTemplate.update(addCourseInviteStatement,
            		newHex,
            		course.getCourseId(),
                    new Timestamp(System.currentTimeMillis()));
            if (rowsAffected == 1) {
            	InviteModel invite = new InviteModel();
            	invite.setHex(newHex);
            	return invite;
            } else {
//            	CourseModel errorCourse = new CourseModel();
//            	errorCourse.setErrorCode(HttpStatus.CONFLICT.value());
//            	return errorCourse;
            }
        } catch (IncorrectResultSizeDataAccessException e){
//        	CourseModel errorCourse = new CourseModel();
//        	errorCourse.setErrorCode(HttpStatus.NOT_FOUND.value());
//        	return errorCourse;
        } catch (DataAccessException e1){
//            CourseModel errorCourse = new CourseModel();
//        	errorCourse.setErrorCode(HttpStatus.CONFLICT.value());
//        	return errorCourse;
        }
        
        return new InviteModel();
    }

    public InviteModel joinCourseThroughInvite(Integer userId, String hex) {
    	InviteModel invite = getCourseThroughInvite(hex);
    	if(invite.getCourse()!=null) {
    		if(!(participantDAO.addParticipant(
    				userId.toString(),
    				invite.getCourse().getCourseId().toString(),
    				"student"))) {
    			invite.getCourse().setCourseId(null);
    			invite.getCourse().setErrorCode(HttpStatus.CONFLICT.value());
    		}
    			
    	}
    	return invite;
    }
    
	public InviteModel getCourseThroughInvite(String hex) {
		try {
			String getCourseThroughInviteStatement =
	                "SELECT * FROM CourseInvite WHERE Hex=?";
            Map<String, Object> map = jdbcTemplate.queryForMap(
            		getCourseThroughInviteStatement, hex);
            
            Timestamp creationDate = (Timestamp) map.get("CreationDate");
            if(isTimedOut(creationDate)) {
            	removeCourseInvite(hex);
            	return new InviteModel();
            } else {
            	InviteModel invite = new InviteModel();
            	invite.setCourse(courseDAO.getCourse((Integer) map.get("CourseId")));
            	invite.setHex(hex);
            	return invite;
            }
		} catch (IncorrectResultSizeDataAccessException e) {
			return new InviteModel();
		} catch (DataAccessException e1) {
			return new InviteModel();
		}
		
	}
	
	private boolean isTimedOut(Timestamp timestamp) {
		return ((timestamp.getTime()+INVITE_DURATION) 
				< System.currentTimeMillis());
	}
	
	public InviteModel getCourseInvite(CourseModel course) {
		try {
			String getCourseThroughInviteStatement =
	                "SELECT * FROM CourseInvite WHERE CourseId=?";
            Map<String, Object> map = jdbcTemplate.queryForMap(
            		getCourseThroughInviteStatement, course.getCourseId());
            
            Timestamp creationDate = (Timestamp) map.get("CreationDate");
            if(isTimedOut(creationDate)) {
            	removeCourseInvite(course);
            	return new InviteModel();
            } else {
            	InviteModel invite = new InviteModel();
            	invite.setHex((String) map.get("Hex"));
            	return invite;
            }
		} catch (IncorrectResultSizeDataAccessException e) {
			return new InviteModel();
		} catch (DataAccessException e1) {
			return new InviteModel();
		}
	}
	
	public Boolean removeCourseInvite(String hex) {
		String removeCourseInviteStatement = "DELETE FROM Course WHERE Hex=?";

        try {
            int rowsAffected = jdbcTemplate.update(removeCourseInviteStatement,
                    hex);         
            if(rowsAffected == 1) {
            	return true;
            }
        } catch (IncorrectResultSizeDataAccessException e){
        } catch (DataAccessException e1){
        }
        return false;
	}
	
	public Boolean removeCourseInvite(CourseModel course) {
		String removeCourseInviteStatement = "DELETE FROM Course WHERE CourseId=?";

        try {
            int rowsAffected = jdbcTemplate.update(removeCourseInviteStatement,
                    course.getCourseId());         
            if(rowsAffected == 1) {
            	return true;
            }
        } catch (IncorrectResultSizeDataAccessException e){
        } catch (DataAccessException e1){
        }
        return false;
	}
}
