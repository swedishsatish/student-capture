package studentcapture.datalayer.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import studentcapture.config.StudentCaptureApplicationTests;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by c13gan on 2016-04-27.
 */
public class SubmissionTest extends StudentCaptureApplicationTests {

    @Autowired
    Submission sub;

    // TODO: Replace jdbctemplate with appropriate class for inserting values into the database
    @Autowired
    JdbcTemplate jdbcTemplate;
    Hashtable theGrade;

    @Before
    public void setUp() throws Exception {

        //INSERTING A USER
        jdbcTemplate.update("INSERT INTO users (userid , firstname , lastname , persnr, pswd ) " +
                "VALUES ('1337', 'Gustav', 'Gustavsson','1234567890', 'X')");

        //INSERTING A COURSE
        jdbcTemplate.update("INSERT INTO course (courseid , year , term , coursecode , coursename ) " +
                "VALUES ('1', '2018', 'vt18',   '5dv742','Databaser i skärmar')");

        //INSERTING TWO ASSIGNMENTS
        jdbcTemplate.update("INSERT INTO assignment (assignmentid , courseid ,title, startdate , enddate,mintime,maxtime, published ) " +
                "VALUES ('10', '1', 'Hämta din databas i 3D', '2018-10-19 10:23:54','2018-10-19 10:23:55', '1','2',  'true')");
        jdbcTemplate.update("INSERT INTO assignment (assignmentid , courseid ,title, startdate , enddate,mintime,maxtime, published ) " +
                "VALUES ('11', '1', 'Häftig data i 4D', '2018-10-19 10:23:54','2018-10-19 10:23:55', '1','2',  'true')");

        //INSERTING A SUBMISSION WITH GRADE
        jdbcTemplate.update("INSERT INTO Submission (assignmentid, studentid, submissiondate, grade, teacherid) " +
                "VALUES ('10', '1337', '2018-10-19 10:23:55', 'vg', '1337');");

        //INSERTING A SUBMISSION WITHOUT GRADE
        jdbcTemplate.update("INSERT INTO Submission (assignmentid, studentid, submissiondate) " +
                "VALUES ('11', '1337', '2018-10-19 10:23:55');");
    }

    @After
    public void tearDown() throws Exception {
        jdbcTemplate.update("DELETE FROM submission WHERE studentid = '1337' AND assignmentid = '10' ");
        jdbcTemplate.update("DELETE FROM submission WHERE studentid = '1337' AND assignmentid = '11' ");

        jdbcTemplate.update("DELETE FROM assignment WHERE assignmentid = '10' ");
        jdbcTemplate.update("DELETE FROM assignment WHERE assignmentid = '11' ");

        jdbcTemplate.update("DELETE FROM course WHERE courseid = '1' ");

        jdbcTemplate.update("DELETE FROM users WHERE userid = '1337' ");

    }

    @Test
    public void shouldReturnGrade() throws Exception {
        theGrade = sub.getGrade("1337", "10");
        assertEquals("vg",theGrade.get("grade"));
    }

    @Test
    public void shouldReturnNothingWhenAssignmentNotExists() throws Exception {
        theGrade = sub.getGrade("1337", "5");
        assertEquals("Query found no data", theGrade.get("grade"));
    }


    @Test
    public void shouldReturnNothingGradeUserNotExists() throws Exception {
        theGrade = sub.getGrade("5", "10");
        assertEquals("Query found no data", theGrade.get("grade"));
    }


    @Test
    public void shouldReturnNothingGradeUserAndAssignmentNotExists() throws Exception {
        theGrade = sub.getGrade("5", "5");
        assertEquals("Query found no data", theGrade.get("grade"));
    }

    @Test
    public void shouldReturnMissingGrade() throws Exception {
        theGrade = sub.getGrade("1337", "11");
        assertEquals("Missing grade", theGrade.get("grade"));
    }

    @Test
    public void shouldReturnCorrectTimeStamp() {
        theGrade = sub.getGrade("1337", "10");
        assertEquals("2018-10-19 10:23:55", theGrade.get("time"));
    }

    @Test
    public void shouldReturnCorrectGrader() {
        theGrade = sub.getGrade("1337", "10");
        assertEquals("Gustav", theGrade.get("teacher"));
    }

    @Test
    public void shouldReturnNoGraderOnNoGrade() throws Exception {
        theGrade = sub.getGrade("1337","11");
        assertEquals("Missing Grader", theGrade.get("teacher"));
    }
}
