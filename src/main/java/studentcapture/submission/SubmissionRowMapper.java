package studentcapture.submission;

import org.springframework.jdbc.core.RowMapper;
import studentcapture.model.Grade;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by c13gan on 2016-05-18.
 */

/**
 * A helpclass to retrieve the information from the database for a submission.
 */
public class SubmissionRowMapper implements RowMapper<Submission> {

    /**
     * A rowmapper to get all the desired information from the submissiontable
     * in the database. Sets booleans to false if they are null and sets ints
     * to 0 if they are null.
     * @param resultSet The resultset that contains the information from the
     *                  submission table.
     * @param i The number of rows in the resultset
     * @return A submission with the information from the database table
     *         submission.
     * @throws SQLException
     */
    @Override
    public Submission mapRow(ResultSet resultSet, int i) throws SQLException {
        Submission submission = new Submission();
        Grade grade = new Grade();

        submission.setAssignmentID(resultSet.getInt("AssignmentId"));
        submission.setStudentID(resultSet.getInt("StudentId"));
        submission.setStudentPublishConsent(resultSet.getBoolean("StudentPublishConsent"));
        submission.setStatus(resultSet.getString("Status"));
        submission.setSubmissionDate(resultSet.getTimestamp("SubmissionDate"));
        grade.setGrade(resultSet.getString("Grade"));
        if (resultSet.getInt("TeacherID") != 0) {
            grade.setTeacherID(resultSet.getInt("TeacherID"));
        }
        grade.setFeedbackIsVisible(resultSet.getBoolean("PublishFeedback"));
        submission.setGrade(grade);
        submission.setPublishStudentSubmission(resultSet.getBoolean("PublishStudentSubmission"));


        return submission;
    }
}
