package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

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

    protected List<Object> getGrade(String studentID, String assID) {
        return null;
    }

    /**

     * Get all ungraded submissions for an assignment

     * @param assID The assignment to get submissions for

     * @return A list of ungraded submissions for the assignment

     */

    protected List<Object> getAllUngraded(String assID) {
        return null;
    }

    /**

     * Get all submissions for an assignment

     * @param assID The assignment to get submissions for

     * @return A list of submissions for the assignment

     */

    protected List<Object> getAllSubmissions(String assID) {
        return null;
    }

}

