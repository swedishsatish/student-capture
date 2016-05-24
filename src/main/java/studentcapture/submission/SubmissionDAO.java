package studentcapture.submission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import studentcapture.datalayer.filesystem.FilesystemConstants;
import studentcapture.datalayer.filesystem.FilesystemInterface;
import studentcapture.model.Grade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SubmissionDAO {

	// This template should be used to send queries to the database
	@Autowired
	protected JdbcTemplate databaseConnection;

	/**
	 * Add a new submission for an assignment
	 *
	 *
	 * @param submission
	 * @return True if everything went well, otherwise false
     * 
     * @author tfy12hsm
	 */
	public boolean addSubmission(Submission submission, Boolean studentConsent) {
		String sql = "INSERT INTO Submission (assignmentId, studentId, SubmissionDate, studentpublishconsent, status)" +
					" VALUES  (?,?,?,?,?)";
		java.util.Date date = new java.util.Date(System.currentTimeMillis());
		java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
		timestamp.setNanos(0);

		int rowsAffected = 0;
		try {
			rowsAffected = databaseConnection.update(sql, submission.getAssignmentID(),
                                                            submission.getStudentID(),
                                                            timestamp,
                                                            studentConsent,
                                                            submission.getStatus());
		} catch (DataAccessException e) {
			return false;
		}
		if(submission.getStudentVideo() != null) {
            FilesystemInterface.storeStudentVideo(submission, submission.getStudentVideo());
        }

		return rowsAffected == 1;
	}

    /**
     * Patch a submission for an assignment
     *
     * @param submission the submission to patch
     * @return True if everything went well, otherwise false
     */
    public boolean patchSubmission(Submission submission) {
		String feedback = submission.getFeedback();
		if (feedback != null && !feedback.isEmpty()) {
			String path = FilesystemInterface.generatePath(submission) + FilesystemConstants.FEEDBACK_TEXT_FILENAME;
			FilesystemInterface.printTextToFile(feedback, path);
		}

        String sql = "UPDATE Submission SET ";
        ArrayList<Object> sqlparams = new ArrayList<>();
        if (submission.getStudentPublishConsent() != null) {
            sql += "studentpublishconsent = ?,";
            sqlparams.add(submission.getStudentPublishConsent());
        }
        if (submission.getStatus() != null) {
            sql += "status = ?,";
            sqlparams.add(submission.getStatus());
        }
        /*if (submission.getGrade() != null) {
            if  (submission.getGrade().getGrade() != null) {
                sql += "grade = ?,";
                sqlparams.add(submission.getGrade().getGrade());
            }
            if  (submission.getGrade().getTeacherID() != null) {
                sql += "teacherid = ?,";
                sqlparams.add(submission.getGrade().getTeacherID());
            }
			if (submission.getGrade().getFeedbackIsVisible() != null) {
				sql += "publishfeedback = ?,";
				sqlparams.add(submission.getGrade().getFeedbackIsVisible());
			}
		}
        if (submission.getPublishStudentSubmission() != null) {
            sql += "publishstudentsubmission = ?,";
            sqlparams.add(submission.getPublishStudentSubmission());
        }
		*/
        if (sqlparams.isEmpty()) {
            return false; // Nothing to patch
        }
        sql = sql.substring(0,sql.length()-1);

        sql += " WHERE assignmentid = ? AND studentid = ?";
        sqlparams.add(submission.getAssignmentID());
        sqlparams.add(submission.getStudentID());

        try {
            return databaseConnection.update(sql, sqlparams.toArray()) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    /**
     * Make the feedback visible for the student
     * @param submission Submission object
     * @return True if a row was changed, otherwise false
	 * @throws IllegalAccessError Cant publish feedback if not a teacher.
     */
    public boolean publishFeedback(Submission submission, boolean publish)
														throws IllegalAccessException {
        /* Publishing feedback without a grade is not possible, returns false */
        Grade grade = submission.getGrade();
        System.out.println("GRADE: " + grade);
        if (grade == null) {
			return false;
		}
        /* If a person that is not a teacher tries to set a grade, return false */
        String checkIfTeacherExist = "SELECT COUNT(*) FROM Participant WHERE (UserID = ?) AND (CourseID = ?) AND (Function = 'Teacher')";
        int rows = databaseConnection.queryForInt(checkIfTeacherExist, grade.getTeacherID(), submission.getCourseID());
        if(rows != 1) {
			throw new IllegalAccessException("Cant set grade, user not a teacher");
		}

        String publishFeedback  = "UPDATE Submission SET publishFeedback = ? WHERE (AssignmentID = ?) AND (StudentID = ?);";
        int updatedRows = databaseConnection.update(publishFeedback, publish, submission.getAssignmentID(), submission.getStudentID());

        return updatedRows == 1;
    }

	/**
	 * Add a grade for a subsmission
	 *
	 * @param submission Submission object
	 * @return True if a row was changed, otherwise false
	 * @throws IllegalAccessError Cant set grade if not a teacher.
	 */
	public boolean setGrade(Submission submission) throws IllegalAccessException {
		Grade grade = submission.getGrade();
        /* If a person that is not a teacher tries to set a grade, return false */
        String checkIfTeacherExist = "SELECT COUNT(*) FROM Participant WHERE" +
				" (UserID = ?) AND (CourseID = ?) AND (Function = 'Teacher')";
        int rows = databaseConnection.queryForInt(checkIfTeacherExist, grade.getTeacherID(), submission.getCourseID());
        if(rows != 1) {
			throw new IllegalAccessException("Cant set grade, user not a teacher");
		}

		String setGrade  = "UPDATE Submission SET Grade = ?, TeacherID = ?, PublishStudentSubmission = ?" +
				" WHERE (AssignmentID = ?) AND (StudentID = ?);";
		int updatedRows = databaseConnection.update(setGrade, grade.getGrade(),
																grade.getTeacherID(),
																submission.getPublishStudentSubmission(),
																submission.getAssignmentID(),
																submission.getStudentID());

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

		int assignmentId = Integer.parseInt(assId);

        return getSubmissionsFromStatement(getAllUngradedStatement, assignmentId);
    }

    /**
     * Will return the result of a query to the DB.
     * @param statement the string containing the sql statement
     * @param assignmentID ID to specify assignment to get submissions for.
     * @return a list of submissions.
     */
    private Optional<List<Submission>> getSubmissionsFromStatement(String statement, int assignmentID){
        List<Submission> submissions = new ArrayList<>();
        //TODO exceptions should maybe be handled in a better way?
        try {
            List<Map<String, Object>> rows = databaseConnection.queryForList(
                    statement, assignmentID);
            for (Map<String, Object> row : rows) {
                Submission submission = new Submission(row);
                submissions.add(submission);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        } catch (DataAccessException e1) {
            return Optional.empty();
        }
        return Optional.of(submissions);
    }

	/**
	 * Get all submissions for an assignment
	 * @param assignmentID The assignment to get submissions for
	 * @return A list of submissions for the assignment
     * 
     * @author tfy12hsm
	 */
    public List<Submission> getAllSubmissions(int assignmentID) {
    	List<Submission> submissions;

		String getAllSubmissionsStatement = "SELECT * FROM Submission WHERE AssignmentId = ?";
		try {
			submissions = databaseConnection.query(getAllSubmissionsStatement, new SubmissionRowMapper(), assignmentID);
		} catch (IncorrectResultSizeDataAccessException e) {
			return new ArrayList<>();
		} catch (DataAccessException e1) {
			return new ArrayList<>();
		}
    	return submissions;
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

    	return getSubmissionsFromStatement(getAllSubmissionsWithStudentsStatement, assignmentId);
    }
    
    /**
     * Gets an entire submission with the name of the teacher, if the teacher
	 * exists.
     * 
     * @param assignmentId	The assignmentId that the submission is connected
	 *                      to.
     * @param userId		The studentId that the submission is connected to.
     * @return				The submission with the teacher name.
     * 
     * @author tfy12hsm
     */
    public Optional<Submission> getSubmission(int assignmentId, int userId) {
    	Submission result = null;
        String getStudentSubmission =
				"SELECT * FROM Submission WHERE AssignmentId=? AND StudentId=?";
		String getTeacherName =
				"SELECT concat(FirstName, ' ', LastName) FROM Users WHERE UserId=?";

		try {
	        result = databaseConnection.queryForObject(
					getStudentSubmission, new SubmissionRowMapper(), assignmentId, userId);
			if (result.getGrade().getTeacherID() != null) {
				result.setTeacherName(databaseConnection.queryForObject(getTeacherName, new Object[]{result.getGrade().getTeacherID()}, String.class));
			} else if (result.getGrade().getTeacherID() == null) {
				result.setTeacherName(null);
			}

	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}
		result.setFeedback(FilesystemInterface.getFeedbackText(result));
        return Optional.of(result);
	}

	/**
	 * Get a teacher's submitted feedback video for a specific student.
	 * @param submission
     * @return
     */
	public ResponseEntity<InputStreamResource> getFeedbackVideo(Submission submission) {
		Integer courseID = getCourseIDFromAssignmentID(submission.getAssignmentID());
		if(courseID == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else{
			submission.setCourseID(Integer.toString(courseID));
			String path = FilesystemInterface.generatePath(submission) + FilesystemConstants.FEEDBACK_VIDEO_FILENAME;
			return FilesystemInterface.getVideo(path);
		}
	}


	/**
	 *
	 * @param assignmentID
	 * @param studentID
	 * @return
	 */
	public boolean setFeedbackVideo(Submission submission, MultipartFile feedbackVideo) {

		return FilesystemInterface.storeFeedbackVideo(submission, feedbackVideo);


	}

	/**
	 * Retrieves the course id from an assignment by querying the database. Returns null if something went wrong.
	 * @param assignmentID
	 * @return
     */
	private Integer getCourseIDFromAssignmentID(int assignmentID){
		try{
			String getCourseId = "SELECT CourseId FROM Assignment WHERE AssignmentId=?";
			return databaseConnection.queryForObject(getCourseId, new Object[]{assignmentID}, Integer.class);
		}catch(Exception e){
			return null;
		}
	}


}

