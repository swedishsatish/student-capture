package studentcapture.datalayer.database;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


/**
 * Created by c13gan on 2016-04-25.
 */
@Repository
public class DatabaseCommunicator {


    @Autowired
    protected JdbcTemplate jdbcTemplate;


    /**
     * Just a method we used to insert some of the testvalues, feel free to add more or remove them from the DB
     */
    public void insertTestValues() {
        //connectToDB();
        jdbcTemplate.update("INSERT INTO users (userid , firstname , lastname , personnummer , casid , password) " +
                "VALUES ('1337',   'Gustav',   'Gustavsson','1234567890', 'c13ggn', 'X')"

        );

        jdbcTemplate.update("INSERT INTO course (courseid , term , coursecode , coursename ) " +
                "VALUES ('1',   'vt18',   '5dv742','Databaser i skärmar')");

        jdbcTemplate.update("INSERT INTO assignment (assignmentid , courseid , startdate , enddate, pathtovideo, " +
                "published ) " +
                "VALUES ('10', '1', '2018-10-19 10:23:54','2018-10-19 10:23:55', '/video/techervid', 'true')");

        jdbcTemplate.update("INSERT INTO Submission (assignmentid, studentid, pathtovideo, grade, teacherid) " +
                "VALUES ('10', '1337', '/videopath/video', 'vg', '1337');");
    }

    /**
     * Not sure if this matches the new database tables we set up, this is just an example to extract one value from
     * the DB
     * @param studID        User ID for the student
     * @param assignmentID  Assigment ID for the specific assignment
     * @return              The grade for that specific assignment and user or //TODO what if no grade exists
     */
    public String returnGrade(int studID, int assignmentID) {
       // connectToDB();
        String getGrade = "SELECT grade FROM submission WHERE (studentid = ? AND assignmentid = ?)";
        String grade;
        try {
            grade = jdbcTemplate.queryForObject(getGrade, new Object[] {studID, assignmentID},
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



        return grade;
    }

}