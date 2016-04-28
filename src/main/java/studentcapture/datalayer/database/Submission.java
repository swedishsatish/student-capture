package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.datalayer.database.Participant.ParticipantWrapper;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class Submission {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**

     * Add a new submission for an assignment

     * @param assID Unique identifier for the assignment we're submitting to

     * @param studentID Unique identifier for the student submitting

     * @param date Date of the submission

     * @return True if everything went well, otherwise false

     */

    protected boolean addSubmission(String assID, String studentID, Date date) {
        return true;
    }

    /**

     * Add a grade for a submission

     * @param assID Unique identifier for the assignment with the submission being graded

     * @param teacherID Unique identifier for the teacher grading

     * @param studentID Unique identifier for the student being graded

     * @param grade The grade of the submission

     * @param date Date of the grading

     * @return True if everything went well, otherwise false

     */

    protected boolean gradeSubmission(String assID, String teacherID, String studentID, String grade, Date date) {
        return false;
    }

    /**

     * Remove a submission

     * @param assID Unique identifier for the assignment with the submission being removed

     * @param studentID Unique identifier for the student whose submission is removed

     * @return True if everything went well, otherwise false

     */

    protected boolean removeSubmission(String assID, String studentID) {
        return false;
    }

    /**

     * Changes the grade of a submission

     * @param assID Unique identifier for the assignment with the submission being graded

     * @param teacherID Unique identifier of the teacher updating

     * @param studentID Unique identifier for the student

     * @param grade The new grade of the submission

     * @param date The date the grade was updated

     * @return True if everything went well, otherwise false

     */

    protected boolean updateGrade(String assID, String teacherID, String studentID, String grade, Date date) {
        return true;
    }

    /**

     * Get information about the grade of a submission

     * @param assID Unique identifier for the assignment submission grade bra

     * @param studentID Unique identifier for the student associated with the submission

     * @return A list containing the grade, date, and grader

     */

    protected ArrayList<Object> getGrade(String studentID, String assID) {
        int studIDInt = Integer.parseInt(studentID);
        int assIDInt = Integer.parseInt(assID);
        ArrayList<Object> returnValues = new ArrayList(3);
        ArrayList<String> queriesToSend = new ArrayList(3);
        String getGrade = "SELECT grade FROM submission WHERE (studentid = ? AND assignmentid = ?)";
        queriesToSend.add(getGrade);
        String getTimeStamp = "SELECT submissiondate FROM submission WHERE (studentid = ? AND assignmentid = ?)";
        queriesToSend.add(getTimeStamp);
        String getTeacherName = "SELECT firstname FROM submission JOIN users ON (techerid = userid) WHERE (studentid = ? AND assignment = ?)";
        queriesToSend.add(getTeacherName);


        String getData = "SELECT grade, submissiondate, firstname FROM submission JOIN users ON (teacherid = userid)" +
                " WHERE (assignmentid = ? AND studentid = ?);";

        for (String s : queriesToSend) {
            try {
                String grade = jdbcTemplate.queryForObject(s, new Object[]{studIDInt, assIDInt}, String.class);
                if (grade == null) {
                    returnValues.add("Missing grade");
                } else {
                    grade = grade.trim();
                    returnValues.add(grade);
                }
            } catch (IncorrectResultSizeDataAccessException e) {
                returnValues.add("Query found no data");
                break;
            } catch (DataAccessException e1) {
                returnValues.add("Dataaccess");
                break;
            }
        }
        return returnValues;

    }

    /**

     * Get all ungraded submissions for an assignment

     * @param assID The assignment to get submissions for

     * @return A list of ungraded submissions for the assignment

     */
    private final static String getAllUngradedStatement = "SELECT * FROM "
    		+ "Submission WHERE (AssignmentId=?) AND (Grade IS NULL)";
    protected List<SubmissionWrapper> getAllUngraded(String assId) {
    	List<SubmissionWrapper> submissions = new ArrayList<>();

    	try {
	    	List<Map<String, Object>> rows = jdbcTemplate.queryForList(
	    			getAllSubmissionsStatement, new Object[] {assId});
	    	for (Map<String, Object> row : rows) {
	    		SubmissionWrapper submission = new SubmissionWrapper();
	    		submission.assignmentId = (int) row.get("AssignmentId");
	    		submission.studentId = (int) row.get("StudentId");
	    		submission.teacherId = (int) row.get("TeacherId");
	    		submission.grade = (String) row.get("teacherId");
	    		submission.submissionTime = (Timestamp)
	    				row.get("SubmissionTime");
	    		submissions.add(submission);
	    	}

	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return null;
		} catch (DataAccessException e1){
			//TODO
			return null;
		}

        return submissions;
    }

    /**

     * Get all submissions for an assignment

     * @param assID The assignment to get submissions for

     * @return A list of submissions for the assignment

     */

    private final static String getAllSubmissionsStatement = "SELECT * FROM "
    		+ "Submission WHERE (AssignmentId=?)";
    protected List<SubmissionWrapper> getAllSubmissions(String assId) {
    	List<SubmissionWrapper> submissions = new ArrayList<>();

    	try {
	    	List<Map<String, Object>> rows = jdbcTemplate.queryForList(
	    			getAllSubmissionsStatement, new Object[] {assId});
	    	for (Map<String, Object> row : rows) {
	    		SubmissionWrapper submission = new SubmissionWrapper();
	    		submission.assignmentId = (int) row.get("AssignmentId");
	    		submission.studentId = (int) row.get("StudentId");
	    		submission.teacherId = (int) row.get("TeacherId");
	    		submission.grade = (String) row.get("teacherId");
	    		submission.submissionTime = (Timestamp)
	    				row.get("SubmissionTime");
	    		submissions.add(submission);
	    	}

	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return null;
		} catch (DataAccessException e1){
			//TODO
			return null;
		}

        return submissions;
    }

    public class SubmissionWrapper {
    	public int assignmentId;
    	public int studentId;
    	public Timestamp submissionTime;
    	public String grade;
    	public int teacherId;
    }
}

