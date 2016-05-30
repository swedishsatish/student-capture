package studentcapture.course.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by c13gan on 2016-04-26..
 */

@Repository
public class ParticipantDAO {

    // This template should be used to send queries to the database
	@Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Add a new participant to a course by connecting the tables "User" and "Course" in the database.
     *
     * @param p participant
     * @return true if insertion worked, else false
     *
     */
    public boolean addParticipant(Participant p) {
        String addParticipantStatement = "INSERT INTO Participant VALUES (?,?,?)";
        boolean result;
        try {
            int rowsAffected = jdbcTemplate.update(addParticipantStatement,
                    p.getUserId(), p.getCourseId(), p.getFunction());
            result = rowsAffected == 1;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


    public Optional<List<Participant>> getCourseParticipants(int courseID,String userRole){
        String sqlQuery= getCourseParticipantsQuery(userRole);
        if(sqlQuery == null){
            return Optional.empty();
        }
        try {
            List<Participant> participants = new ArrayList<>();
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlQuery,courseID);
            for (Map<String, Object> row : rows) {
                participants.add(new Participant(row));
            }
            return Optional.of(participants);
        } catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Participant> getCourseParticipant(int courseID,int userID){
        String sql = "SELECT * FROM Participant WHERE (UserID = ? AND CourseID = ?);";
        try{
            Map<String, Object> map = jdbcTemplate.queryForMap(sql,userID,courseID);
            return Optional.of(new Participant(map));
        }
        catch(Exception e){
            return Optional.empty();
        }
    }


    private String getCourseParticipantsQuery(String userRole){
        String tempSql = "SELECT * FROM Participant WHERE courseID = ?";
        String sql = ";";
        if(userRole == null){
            return null;
        }
        if(userRole.compareTo("all roles") == 0){
            return tempSql + sql;
        }
        if(userRole.compareTo("student") == 0){
            sql = " AND Function = 'student';";
        }
        else if(userRole.compareTo("teacher") == 0){
            sql = " AND Function = 'teacher';";
        }
        else if(userRole.compareTo("assistant") == 0){
            sql = " AND Function = 'assistant';";
        }
        else{
            return null;
        }
        return tempSql + sql;
    }





    /**
     * Returns a list of all participants of a course including their function
     *
     * @return              List of tuples: CAS_ID - function
     * 
     */
    public Optional<List<Participant>> getAllParticipantsFromCourse(String courseID){
    	List<Participant> participants = new ArrayList<>();
    	int courseId = Integer.parseInt(courseID);
        String getAllParticipantFromCourseStatement =
                "SELECT * FROM Participant WHERE (CourseId=?)";

		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					getAllParticipantFromCourseStatement, courseId);
			for (Map<String, Object> row : rows) {
				Participant participant = new Participant(row);
				participants.add(participant);
			}

		} catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}

        return Optional.of(participants);
    }

    /**
     * Returns a list of all courses a person is registered for, including their function
     *
     * @param userID        unique identifier for a person
     * @return              List of tuples: CourseID - function
     * 
     */
    public Optional<List<Participant>> getAllCoursesIDsForParticipant(String userID) {
    	List<Participant> participants = new ArrayList<>();
    	int userId = Integer.parseInt(userID);

        String getAllCoursesForParticipantStatement =
                "SELECT * FROM Participant WHERE (UserId=?)";

    	try {
	    	List<Map<String, Object>> rows = jdbcTemplate.queryForList(
	    			getAllCoursesForParticipantStatement, userId);
	    	for (Map<String, Object> row : rows) {
	    		Participant participant = new Participant(row);
	    		participants.add(participant);
	    	}

	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}

        return Optional.of(participants);
    }


    /**
     * Removes the connection between a person and a course from the database
     *
     * @param userID        unique identifier for a person
     * @return              true if removal worked, else false
     * 
     */
    public boolean removeParticipant(String userID, String courseID){
    	boolean result;
    	int userId = Integer.parseInt(userID);
    	int courseId = Integer.parseInt(courseID);
        String removeParticipantStatement =
                "DELETE FROM Participant WHERE (UserId=? AND CourseId=?)";

        try {
            int rowsAffected = jdbcTemplate.update(removeParticipantStatement,
                    userId, courseId);
            result = rowsAffected == 1;
        } catch (DataAccessException e1){
            result = false;
        }
        return result;
    }

    /**
     * Updates a participant's function in the database.
     *
     * @param participant object containing participant information
     * @return true if insertion worked, else false
     */
    public boolean setParticipantFunction(Participant participant) {
        String editParticipantFunctionStatement = "UPDATE participant SET function=? WHERE userid=? AND courseid=?";
        boolean result;

        try {
            int rowsAffected = jdbcTemplate.update(editParticipantFunctionStatement,
                    participant.getFunction(), participant.getUserId(), participant.getCourseId());
            result = rowsAffected == 1;
        } catch (DataAccessException e1) {
            result = false;
        }

        return result;
    }
    
    public boolean hasTeacherPermission(Integer userId) {
    	String statement = "SELECT isteacher FROM Users WHERE (UserId=?) AND "
    			+ "(isteacher=true)";
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(
    			statement, userId);
    	return (result.size() > 0);
    }
    
    public boolean isParticipantOnCourse(Integer userId, Integer courseId) {
    	String statement = "SELECT * FROM Participant WHERE (UserId=?) AND "
    			+ "(CourseId=?)";
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(
    			statement, userId, courseId);
    	return (result.size() > 0);
    }
    
    public boolean isTeacherOnCourse(Integer userId, Integer courseId) {
    	String statement = "SELECT * FROM Participant WHERE (UserId=?) AND "
    			+ "(CourseId=?) AND (Function='teacher')";
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(
    			statement, userId, courseId);
    	return (result.size() > 0);
    }
    
    public boolean isStudentOnCourse(Integer userId, Integer courseId) {
    	String statement = "SELECT * FROM Participant WHERE (UserId=?) AND "
    			+ "(CourseId=?) AND (Function='student')";
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(
    			statement, userId, courseId);
    	return (result.size() > 0);
    }
    
    public boolean isTeacherOnAssignment(Integer userId, Integer assignmentId) {
    	String statement = "SELECT * FROM Participant AS par INNER JOIN "
    			+ "Assignment AS ass ON par.courseId=ass.courseId WHERE "
    			+ "(userId=?) AND (assignmentId=?) AND (function='teacher')";
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(
    			statement, userId, assignmentId);
    	return (result.size() > 0);
    }
    
    public boolean isStudentOnAssignment(Integer userId, Integer assignmentId) {
    	String statement = "SELECT * FROM Participant AS par INNER JOIN "
    			+ "Assignment AS ass ON par.courseId=ass.courseId WHERE "
    			+ "(userId=?) AND (assignmentId=?) AND (function='student')";
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(
    			statement, userId, assignmentId);
    	return (result.size() > 0);
    }
    
    public boolean isSubmissionTeacher(Integer teacherId, Integer studentId, 
    		Integer assignmentId) {
    	String statement = "SELECT * FROM Participant AS par INNER JOIN "
    			+ "Assignment AS ass ON par.courseId=ass.courseId INNER JOIN"
    			+ " Submission AS sub ON ass.assignmentId=sub.assignmentId WHERE "
    			+ "(userId=?) AND (ass.assignmentId=?) AND (studentId=?) AND "
    			+ "(function='teacher')";
    	List<Map<String, Object>> result = jdbcTemplate.queryForList(
    			statement, teacherId, assignmentId, studentId);
    	return (result.size() > 0);
    }
    
}
