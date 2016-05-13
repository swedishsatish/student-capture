package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import studentcapture.feedback.FeedbackModel;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class SubmissionDAO {

	// This template should be used to send queries to the database
	@Autowired
	protected JdbcTemplate databaseConnection;

	/**
	 * Add a new submission for an assignment
	 *
	 * @param assignmentID Unique identifier for the assignment we're submitting to
	 * @param studentID    Unique identifier for the student submitting
	 * @return True if everything went well, otherwise false
     * 
     * @author tfy12hsm
	 */
	public boolean addSubmission(String assignmentID, String studentID, Boolean studentConsent) {
		String sql = "INSERT INTO Submission (assignmentId, studentId, SubmissionDate, studentConsent) VALUES  (?,?,?,?)";
		java.util.Date date = new java.util.Date(System.currentTimeMillis());
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		timestamp.setNanos(0);

		int rowsAffected = databaseConnection.update(sql, Integer.parseInt(assignmentID), Integer.parseInt(studentID), timestamp, studentConsent);

		return rowsAffected == 1;
	}

    /**
     * Make the feedback visible for the student
     * @param submission
     * @return
     */
    public boolean publishFeedback(Submission submission, boolean publish) {
        /* Publishing feedback without a grade is not possible, returns false */
        Grade grade = submission.getGrade();
        if (grade == null)
            return false;

        /* If a person that is not a teacher tries to set a grade, return false */
        String checkIfTeacherExist = "SELECT COUNT(*) FROM Participant WHERE (UserID = ?) AND (CourseID = ?) AND (Function = 'Teacher')";
        int rows = databaseConnection.queryForInt(checkIfTeacherExist, grade.getTeacherID(), submission.getCourseID());
        if(rows != 1)
            return false;

        String publishFeedback  = "UPDATE Submission SET publishFeedback = ? WHERE (AssignmentID = ?) AND (StudentID = ?);";
        int updatedRows = databaseConnection.update(publishFeedback, publish, submission.getAssignmentID(), submission.getStudentID());

        return updatedRows == 1;
    }

	/**
	 * Add a grade for a subsmission
	 *
	 * @param submission Object containing assignmentID, studentID
	 * @param grade      Object containing grade, teacherID, date, publish
	 * @return True if a row was changed, otherwise false
	 */
	public boolean setGrade(Submission submission, Grade grade) {
        /* If a person that is not a teacher tries to set a grade, return false */
        String checkIfTeacherExist = "SELECT COUNT(*) FROM Participant WHERE (UserID = ?) AND (CourseID = ?) AND (Function = 'Teacher')";
        int rows = databaseConnection.queryForInt(checkIfTeacherExist, grade.getTeacherID(), submission.getCourseID());
        if(rows != 1)
            return false;

		String setGrade  = "UPDATE Submission SET Grade = ?, TeacherID = ?, SubmissionDate = ?, PublishStudentSubmission = ? WHERE (AssignmentID = ?) AND (StudentID = ?);";
		int updatedRows = databaseConnection.update(setGrade, grade.getGrade(), grade.getTeacherID(), grade.getDate(), grade.getPublishStudentSubmission(), submission.getAssignmentID(), submission.getStudentID());

		return updatedRows == 1;
	}

	

	/**
	 * Remove a submission
	 *
	 * @param assID     Unique identifier for the assignment with the submission being removed
	 * @param studentID Unique identifier for the student whose submission is removed
	 * @return True if everything went well, otherwise false
     * 
     * @author tfy12hsm
	 */
	public boolean removeSubmission(String assID, String studentID) {
		String removeSubmissionStatement = "DELETE FROM "
				+ "Submission WHERE (AssignmentId=? AND StudentId=?)";
		boolean result;
		int assignmentId = Integer.parseInt(assID);
		int studentId = Integer.parseInt(studentID);

		try {
			int rowsAffected = databaseConnection.update(removeSubmissionStatement,
					assignmentId, studentId);
			result = rowsAffected == 1;
		} catch (IncorrectResultSizeDataAccessException e) {
			result = false;
		} catch (DataAccessException e1) {
			result = false;
		}

		return result;
	}

	/**
	 * Get information about the grade of a submission
	 *
	 * @param assignmentID Unique identifier for the assignment submission grade bra
	 * @param studentID    Unique identifier for the student associated with the submission
	 * @return A list containing the grade, date, and grader
	 */
	public Map<String, Object> getGrade(FeedbackModel model) {
		String queryForGrade = "SELECT grade, submissiondate as time, " +
				"concat(firstname,' ', lastname) as teacher FROM " +
				"submission FULL OUTER JOIN users ON (teacherid = userid)" +
				" WHERE (studentid = ? AND assignmentid = ?)";
		Map<String, Object> response;
		try {
			response = databaseConnection.queryForMap(queryForGrade,
					new Object[]{model.getStudentID(), model.getAssignmentID()});
		response.put("time", response.get("time").toString());
		if (response.get("teacher").equals(" "))
			response.put("teacher", null);
		} catch(IncorrectResultSizeDataAccessException e) {
			response = new HashMap<>();
			response.put("error", "The given parameters does not have an" +
				" entry in the database");
		} catch(DataAccessException e) {
			response = new HashMap<>();
			response.put("error", "Could not connect to the database");
		}

		return response;
	}

    /**
     * Get all ungraded submissions for an assignment
     *
     * @param assId The assignment to get submissions for
     * @return A list of ungraded submissions for the assignment
     * 
     * @author tfy12hsm
     */
    public Optional<List<Submission>> getAllUngraded(String assId) {

		String getAllUngradedStatement = "SELECT "
				+ "sub.AssignmentId,sub.StudentId,stu.FirstName,stu.LastName,"
				+ "sub.SubmissionDate,sub.Grade,sub.TeacherId,"
				+ "sub.StudentPublishConsent,sub.PublishStudentSubmission FROM"
				+ " Submission AS sub LEFT JOIN Users AS stu ON "
				+ "sub.studentId=stu.userId WHERE (AssignmentId=?) AND "
				+ "(Grade IS NULL)";

    	List<Submission> submissions = new ArrayList<>();
    	int assignmentId = Integer.parseInt(assId);
    	try {
	    	List<Map<String, Object>> rows = databaseConnection.queryForList(
	    			getAllUngradedStatement, assignmentId);
	    	for (Map<String, Object> row : rows) {
	    		Submission submission = new Submission(row);
	    		submissions.add(submission);
	    	}

        } catch (IncorrectResultSizeDataAccessException e) {
            //TODO
            return Optional.empty();
        } catch (DataAccessException e1) {
            //TODO
            return Optional.empty();
        }

        return Optional.of(submissions);
    }

	/**
	 * Get all submissions for an assignment
	 * @param assId The assignment to get submissions for
	 * @return A list of submissions for the assignment
     * 
     * @author tfy12hsm
	 */
    public Optional<List<Submission>> getAllSubmissions(String assId) {
    	List<Submission> submissions = new ArrayList<>();
    	int assignmentId = Integer.parseInt(assId);

		String getAllSubmissionsStatement = "SELECT "
				+ "sub.AssignmentId,sub.StudentId,stu.FirstName,stu.LastName,"
				+ "sub.SubmissionDate,sub.Grade,sub.TeacherId,"
				+ "sub.StudentPublishConsent,sub.PublishStudentSubmission FROM"
				+ " Submission AS sub LEFT JOIN Users AS stu ON "
				+ "sub.studentId=stu.userId WHERE (AssignmentId=?)";

    	try {
	    	List<Map<String, Object>> rows = databaseConnection.queryForList(
	    			getAllSubmissionsStatement, assignmentId);
	    	for (Map<String, Object> row : rows) {
	    		Submission submission = new Submission(row);
	    		submissions.add(submission);
	    	}

	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}

        return Optional.of(submissions);
    }

	/**
	 *
	 * Get all submissions for an assignment, including students that have not
	 * yet made a submission.
	 *
	 * @param assId The assignment to get submissions for
	 * @return A list of submissions for the assignment
     * 
     * @author tfy12hsm
	 */
    public Optional<List<Submission>> getAllSubmissionsWithStudents
    		(String assId) {
    	List<Submission> submissions = new ArrayList<>();
    	int assignmentId = Integer.parseInt(assId);

		String getAllSubmissionsWithStudentsStatement =
				"SELECT ass.AssignmentId,par.UserId AS StudentId,sub.SubmissionDate"
						+ ",sub.Grade,sub.TeacherId,sub.StudentPublishConsent"
						+ ",sub.PublishStudentSubmission FROM Assignment"
						+ " AS ass RIGHT JOIN "
						+ "Participant AS par ON ass.CourseId=par.CourseId LEFT JOIN "
						+ "Submission AS sub ON par.userId=sub.studentId WHERE "
						+ "(par.function='Student') AND (ass.AssignmentId=?)";

    	try {
	    	List<Map<String, Object>> rows = databaseConnection.queryForList(
	    			getAllSubmissionsWithStudentsStatement, assignmentId);
	    	for (Map<String, Object> row : rows) {
	    		Submission submission = new Submission(row);
	    		submissions.add(submission);
	    	}

	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}

        return Optional.of(submissions);
    }
    
    /**
     * Returns a sought submission from the database.
     * 
     * @param assignmentId	assignment identifier
     * @param userId		user identifier
     * @return				sought submission
     * 
     * @author tfy12hsm
     */
    public Optional<Submission> getSubmission(int assignmentId, int userId) {
    	Submission result = null;
    	String getStudentSubmissionStatement =
				"SELECT * FROM Submission WHERE AssignmentId=? AND StudentId=?";

		try {
	    	Map<String, Object> map = databaseConnection.queryForMap(
	    			getStudentSubmissionStatement, assignmentId, userId);
	    	
	    	 result = new Submission(map);
	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}

        return Optional.of(result);
	}

    public static class SubmissionWrapper {
    	public int assignmentId;
    	public int studentId;
    	public String studentName;
    	public String submissionDate;
    	public String grade;
    	public Integer teacherId;
    }
}

