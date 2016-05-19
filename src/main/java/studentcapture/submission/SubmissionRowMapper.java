package studentcapture.submission;

import org.springframework.jdbc.core.RowMapper;
import studentcapture.model.Grade;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by c13gan on 2016-05-18.
 */

public class SubmissionRowMapper implements RowMapper<Submission> {


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
        submission.setGrade(grade);
        submission.setPublishStudentSubmission(resultSet.getBoolean("PublishStudentSubmission"));
        submission.setPublishFeedback(resultSet.getBoolean("PublishFeedback"));

        return submission;
    }
}
