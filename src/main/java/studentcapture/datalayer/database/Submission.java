package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.datalayer.database.Participant.ParticipantWrapper;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class Submission {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Add a new submission for an assignment
     *
     * @param assID     Unique identifier for the assignment we're submitting to
     * @param studentID Unique identifier for the student submitting
     * @return True if everything went well, otherwise false
     */

    private static final String addSubmissionStatement = "INSERT INTO "
    		+ "Submission (AssignmentId,StudentId,SubmissionDate) VALUES "
    		+ "(?,?,?)";
    public boolean addSubmission(String assID, String studentID) {
        boolean result;
        int assignmentId = Integer.parseInt(assID);
    	int studentId = Integer.parseInt(studentID);
        try {
            int rowsAffected = jdbcTemplate.update(addSubmissionStatement,
            		new Object[] {assignmentId, studentId, 
            		new Timestamp(1000*(System.currentTimeMillis()/1000))});
            if(rowsAffected == 1) {
            	result = true;
            } else {
            	result = false;
            }
        }catch (IncorrectResultSizeDataAccessException e){
            result = false;
        }catch (DataAccessException e1){
            result = false;
        }

        return result;
    }

    /**
     * Add a grade for a submission
     *
     * @param assID     Unique identifier for the assignment with the submission being graded
     * @param teacherID Unique identifier for the teacher grading
     * @param studentID Unique identifier for the student being graded
     * @param grade     The grade of the submission
     * @return True if everything went well, otherwise false
     */

    public boolean setGrade(String assID, String teacherID, String studentID, String grade) {
        String setGrade = "UPDATE Submission (Grade, TeacherID, Date) = (?, ?, ?) WHERE AssignmentID = ? AND StudentID = ?";
        //String setGrade = "UPDATE Submission SET Grade = ?, TeacherID = ?, Date = ? WHERE AssignmentID = ? AND StudentID = ?";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        System.out.println("POINT OF FAILURE");
        int updatedRows = jdbcTemplate.update(setGrade, new Object[]{grade, teacherID, dateFormat.format(date), assID, studentID});
        System.out.println("UPDATED ROWS: " + updatedRows);
        if (updatedRows == 1)
            return true;
        else
            return false;
    }

    /**
     * Remove a submission
     *
     * @param assID     Unique identifier for the assignment with the submission being removed
     * @param studentID Unique identifier for the student whose submission is removed
     * @return True if everything went well, otherwise false
     */

    private static final String removeSubmissionStatement = "DELETE FROM "
    		+ "Submission WHERE (AssignmentId=? AND StudentId=?)";
    public boolean removeSubmission(String assID, String studentID) {
    	boolean result;
    	int assignmentId = Integer.parseInt(assID);
    	int studentId = Integer.parseInt(studentID);
        try {
            int rowsAffected = jdbcTemplate.update(removeSubmissionStatement,
            		new Object[] {assignmentId, studentId});
            if(rowsAffected == 1) {
            	result = true;
            } else {
            	result = false;
            }
        }catch (IncorrectResultSizeDataAccessException e){
            result = false;
        }catch (DataAccessException e1){
            result = false;
        }
        return result;
    }

    /**
     * Changes the grade of a submission
     *
     * @param assID     Unique identifier for the assignment with the submission being graded
     * @param teacherID Unique identifier of the teacher updating
     * @param studentID Unique identifier for the student
     * @param grade     The new grade of the submission
     * @param date      The date the grade was updated
     * @return True if everything went well, otherwise false
     */

    public boolean updateGrade(String assID, String teacherID, String studentID, String grade, Date date) {
        return true;
    }

    /**
     * Get information about the grade of a submission
     *
     * @param assID     Unique identifier for the assignment submission grade bra
     * @param studentID Unique identifier for the student associated with the submission
     * @return A list containing the grade, date, and grader
     */

    public Hashtable<String, Object> getGrade(String studentID, String assID) {
        int studIDInt = Integer.parseInt(studentID);
        int assIDInt = Integer.parseInt(assID);
        Hashtable<String, Object> returnValues = new Hashtable<>(3);
        ArrayList<String[]> queriesToSend = new ArrayList(3);

        String getGrade[] = {"grade", "SELECT grade FROM submission WHERE (studentid = ? AND assignmentid = ?)"};
        String getTimeStamp[] = {"time", "SELECT submissiondate FROM submission WHERE (studentid = ? AND assignmentid = ?)"};
        String getTeacherName[] = {"teacher", "SELECT firstname FROM submission JOIN users ON (teacherid = userid) WHERE (studentid = ? AND assignmentid = ?)"};
        queriesToSend.add(getGrade);
        queriesToSend.add(getTimeStamp);
        queriesToSend.add(getTeacherName);


        for (String s[] : queriesToSend) {
            try {
                String grade = null;
                if (s[0].equals("teacher") && !checkTeacherId(assIDInt)) {
                    returnValues.put(s[0], "Missing Grader");

                } else {

                    grade = jdbcTemplate.queryForObject(s[1], new Object[]{studIDInt, assIDInt}, String.class);
                }
                if (grade == null) {
                    switch (s[0]) {

                        case "grade":
                            returnValues.put(s[0], "Missing grade");
                            break;
                        case "time":
                            returnValues.put(s[0], "No timestamp found");
                            break;
                        case "teacher":
                            returnValues.put(s[0], "Missing Grader");
                            break;

                        default:
                            returnValues.put(s[0], "Invalid query key, See Submission.java");
                            break;

                    }
                } else {
                    grade = grade.trim();
                    returnValues.put(s[0], grade);
                }
            } catch (IncorrectResultSizeDataAccessException e) {
                returnValues.put(s[0], "Query found no data");
            } catch (DataAccessException e1) {
                returnValues.put(s[0], "Dataaccess not found");

            }
        }
        return returnValues;

    }

    /**
     * Checks if the submission has a grader
     *
     * @param assID ID of the assignment.
     * @return true if a teacherid exists, else false.
     */
    public boolean checkTeacherId(int assID) {

        String checkForTeacher = "SELECT teacherid FROM submission WHERE ( assignmentid = ?)";
        return jdbcTemplate.queryForObject(checkForTeacher, new Object[]{assID}, String.class) != null;


    }


    /**
     * Get all ungraded submissions for an assignment
     *
     * @param assID The assignment to get submissions for
     * @return A list of ungraded submissions for the assignment
     */
    private final static String getAllUngradedStatement = "SELECT * FROM "
    		+ "Submission WHERE (AssignmentId=?) AND (Grade IS NULL)";
    public Optional<List<SubmissionWrapper>> getAllUngraded(String assId) {
    	List<SubmissionWrapper> submissions = new ArrayList<>();
    	int assignmentId = Integer.parseInt(assId);
    	try {
	    	List<Map<String, Object>> rows = jdbcTemplate.queryForList(
	    			getAllUngradedStatement, new Object[] {assignmentId});
	    	for (Map<String, Object> row : rows) {
	    		SubmissionWrapper submission = new SubmissionWrapper();
	    		submission.assignmentId = (int) row.get("AssignmentId");
	    		submission.studentId = (int) row.get("StudentId");
	    		submission.teacherId = Optional.of((int) row.get("TeacherId"));
	    		submission.grade = Optional.of((String) row.get("Grade"));
	    		submission.submissionDate = ((Timestamp)
	    				row.get("SubmissionDate")).toString();
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

     * Get all submissions for an assignment

     * @param assID The assignment to get submissions for

     * @return A list of submissions for the assignment

     */

    private final static String getAllSubmissionsStatement = "SELECT * FROM "
    		+ "Submission WHERE (AssignmentId=?)";
    public Optional<List<SubmissionWrapper>> getAllSubmissions(String assId) {
    	List<SubmissionWrapper> submissions = new ArrayList<>();
    	int assignmentId = Integer.parseInt(assId);
    	try {
	    	List<Map<String, Object>> rows = jdbcTemplate.queryForList(
	    			getAllSubmissionsStatement, new Object[] {assignmentId});
	    	for (Map<String, Object> row : rows) {
	    		SubmissionWrapper submission = new SubmissionWrapper();
	    		submission.assignmentId = (int) row.get("AssignmentId");
	    		submission.studentId = (int) row.get("StudentId");
	    		try {
	    			submission.teacherId = Optional.of((int) row.get("TeacherId"));
	    		} catch (NullPointerException e) {
	    			submission.teacherId = Optional.empty();
	    		}
	    		try {
	    			submission.grade = Optional.of((String) row.get("Grade"));
	    		} catch (NullPointerException e) {
	    			submission.grade = Optional.empty();
	    		}
	    		submission.submissionDate = ((Timestamp)
	    				row.get("SubmissionDate")).toString();
	    		
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

    public class SubmissionWrapper {
    	public int assignmentId;
    	public int studentId;
    	public String submissionDate;
    	public Optional<String> grade;
    	public Optional<Integer> teacherId;
    }
}

