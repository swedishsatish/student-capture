package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// import studentcapture.datalayer.database.Participant.ParticipantWrapper;

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

    protected boolean addSubmission(String assID, String studentID) {
        return true;
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

    protected boolean setGrade(String assID, String teacherID, String studentID, String grade) {
        String setGrade = "UPDATE Submission (Grade, TeacherID, Date) = (?, ?, ?) WHERE (AssignmentID = ?) AND (StudentID = ?)";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        int updatedRows = jdbcTemplate.update(setGrade, new Object[]{grade, teacherID, dateFormat.format(date), assID, studentID});
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

    protected boolean removeSubmission(String assID, String studentID) {
        return false;
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

    protected boolean updateGrade(String assID, String teacherID, String studentID, String grade, Date date) {
        return true;
    }

    /**
     * Get information about the grade of a submission
     *
     * @param assignmentID     Unique identifier for the assignment submission grade bra
     * @param studentID Unique identifier for the student associated with the submission
     * @return A list containing the grade, date, and grader
     */

    public Map<String, Object> getGrade(int studentID, int assignmentID) {

        String query = "SELECT grade, submissiondate as time, concat(firstname,' ', lastname) as teacher" +
                       " FROM submission JOIN users ON (teacherid = userid) WHERE (studentid = ? AND assignmentid = ?)";
        Map<String, Object> response;
        try {
            response = jdbcTemplate.queryForMap(query, studentID, assignmentID);
            //return the time as string instead of timestamp
            response.put("time", response.get("time").toString());
        } catch (IncorrectResultSizeDataAccessException e) {
            response = new HashMap<>();
            //TODO create better error message
            response.put("error", e.getMessage());
        } catch (DataAccessException e) {
            response = new HashMap<>();
            //TODO create better error message
            response.put("error", e.getMessage());
        }
        return response;
    }


    /**
     * Get all ungraded submissions for an assignment
     *
     * @param assID The assignment to get submissions for
     * @return A list of ungraded submissions for the assignment
     */
    private final static String getAllUngradedStatement = "SELECT * FROM "
            + "Submission WHERE (AssignmentId=?) AND (Grade IS NULL)";

    protected Optional<List<SubmissionWrapper>> getAllUngraded(String assId) {
        List<SubmissionWrapper> submissions = new ArrayList<>();

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    getAllSubmissionsStatement, new Object[]{assId});
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
     *
     * @param assID The assignment to get submissions for
     * @return A list of submissions for the assignment
     */

    private final static String getAllSubmissionsStatement = "SELECT * FROM "
            + "Submission WHERE (AssignmentId=?)";

    protected Optional<List<SubmissionWrapper>> getAllSubmissions(String assId) {
        List<SubmissionWrapper> submissions = new ArrayList<>();

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    getAllSubmissionsStatement, new Object[]{assId});
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

        } catch (IncorrectResultSizeDataAccessException e) {
            //TODO
            return Optional.empty();
        } catch (DataAccessException e1) {
            //TODO
            return Optional.empty();
        }

        return Optional.of(submissions);
    }

    public class SubmissionWrapper {
        public int assignmentId;
        public int studentId;
        public Timestamp submissionTime;
        public String grade;
        public int teacherId;
    }
}

