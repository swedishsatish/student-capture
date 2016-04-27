package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;

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
        ArrayList returnValues = new ArrayList<Object>();
        String getGrade = "SELECT grade FROM submission WHERE (studentid = ? AND assignmentid = ?)";
        String grade;
        try {
            grade = jdbcTemplate.queryForObject(getGrade, new Object[] {studIDInt, assIDInt},
                    String.class);
            if (grade == null) {
                grade = "Missing grade";
            } else {
                grade = grade.trim();
            }
        }catch (IncorrectResultSizeDataAccessException e){
            grade = "No submission for this user ID and/or assignment ID";
        }catch (DataAccessException e1){
            grade = "No submission for this user ID and/or assignment ID";
        }
        returnValues.add(grade);
        return returnValues;
    }

    public String getString() {
        return "vg";
    }

    public DataSource getDatasource() {
        return jdbcTemplate.getDataSource();

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

