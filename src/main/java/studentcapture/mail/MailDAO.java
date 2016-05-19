package studentcapture.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


/**
 * Author C13evk
 */
@Repository
public class MailDAO {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private String from;
    private String subject;
    private String message;

    public MailDAO(){
        from = "StudentCapture";
        subject = "Assignment Reminder";
        message = createMessage();
    }

    public List<String> getAssignmentID(){
        return jdbcTemplate.queryForList(getAssignmentIDQuery(),String.class);
    }

    public String getStartDateFromAssignment(String assID){
        return jdbcTemplate.queryForObject(getStartDateQuery(), new Object[]{assID}, String.class);
    }

    public String getCourseIDFromAssignment(String assID){
        return jdbcTemplate.queryForObject(getStartDateQuery(), new Object[]{assID}, String.class);
    }




    public List<String> getPraticipantsEmails(String courseID){
        return jdbcTemplate.queryForList(getEmailsQuery(), new Object[]{courseID}, String.class);
    }


    private String getEmailsQuery() {
        return "SELECT Users.email "+
                "FROM Users JOIN Participant ON Users.userID = Participant.userID "+
                "WHERE courseID = ?;";
    }

    private String getAssignmentQuery() {
        return "SELECT * FROM assignment;";
    }

    private String getAssignmentIDQuery(){
        return "SELECT assignmentID FROM assignment;";
    }

    private String getStartDateQuery(){
        return "SELECT startdate FROM assignment WHERE assignmentID=?;";
    }

    private String getCourseIDQuery(){
        return "SELECT courseID FROM assignment WHERE assignmentID=?;";
    }

    private String createMessage(){
        return "Test";
    }

}
