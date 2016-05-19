package studentcapture.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


/**
 * Author C13evk
 */
@Repository
public class MailDAO {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    public MailDAO(){

    }

    public Optional<List<String>> getAssignmentID(){
        List<String> assIDList;
        try {
            assIDList = jdbcTemplate.queryForList(getAssignmentIDQuery(),String.class);
        }catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(assIDList);
    }

    public Optional<String> getStartDateFromAssignment(String assID){
        String date;
        try {
            date = jdbcTemplate.queryForObject(getStartDateQuery(), new Object[]{assID}, String.class);
        } catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(date);

    }

    public Optional<String> getCourseIDFromAssignment(String assID){
        String courseID;
        try {
            courseID = jdbcTemplate.queryForObject(getCourseIDQuery(), new Object[]{assID}, String.class);
        } catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(courseID);
    }

    public Optional<List<String>> getPraticipantsEmails(String courseID){
        List<String> emailList;
        try{
            emailList = jdbcTemplate.queryForList(getEmailsQuery(), new Object[]{courseID}, String.class);
        } catch (IncorrectResultSizeDataAccessException e){
            return Optional.empty();
        } catch (DataAccessException e1){
            return Optional.empty();
        }
        return Optional.of(emailList);
    }


    private String getEmailsQuery() {
        return "SELECT Users.email "+
                "FROM Users JOIN Participant ON Users.userID = Participant.userID "+
                "WHERE courseID = ?;";
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
}
