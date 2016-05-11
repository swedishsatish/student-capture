package studentcapture.datalayer.database;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.datalayer.database.Submission.SubmissionWrapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by c12osn on 2016-05-02.
 */
public class SubmissionTest  extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    //jdbcTemplate.queryForObject(s[1], new Object[]{studIDInt, assIDInt}, String.class);

    @Autowired
    Submission sub;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Before
    public void setUp() {
        Mockito.reset(jdbcMock);
    }

    @Test
    public void shouldBeAbleToConnectToDB(){
        String sqlQuery = "SELECT grade, submissiondate as time, " +
                "concat(firstname,' ', lastname) as teacher FROM " +
                "submission FULL OUTER JOIN users ON (teacherid = userid)" +
                " WHERE (studentid = ? AND assignmentid = ?)";

        Map responseFromMock = new HashMap();
        responseFromMock.put("grade", "vg");
        responseFromMock.put("time", "10100101");
        responseFromMock.put("teacher", "Lillis");


        when(jdbcMock.queryForMap(sqlQuery, 1, 1)).
                thenReturn(responseFromMock);
        Map<String, Object> response = sub.getGrade(1, 1);

        assertEquals("vg", response.get("grade"));
    }

    /*@Test
    public void setGradeTest() {
        String getAllSubmissionsStatement = "SELECT "
                + "sub.AssignmentId,sub.StudentId,stu.FirstName,stu.LastName,"
                + "sub.SubmissionDate,sub.Grade,sub.TeacherId FROM "
                + "Submission AS sub LEFT JOIN Users AS stu ON "
                + "sub.studentId=stu.userId WHERE (AssignmentId=?)";

        Map responseFromMock = new HashMap<>();
        responseFromMock.put("AssignmentID", 1);
        responseFromMock.put("StudentId", 3);
        responseFromMock.put("TeacherId", 2);
        responseFromMock.put("Grade", "VG");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Date date = new Date();
        responseFromMock.put("Date", dateFormat.format(date));

        sub.setGrade(1, 2, 3, "VG");

        List<Map<String, Object>> listFromMock = new ArrayList<>();
        listFromMock.add(responseFromMock);

        when(jdbcMock.queryForList(getAllSubmissionsStatement, 1)).
                thenReturn(listFromMock);

        List<SubmissionWrapper> response = sub.getAllSubmissions("1").get();

        assertEquals(response.get(0).assignmentId,1);
        assertEquals(response.get(0).studentId,3);
        assertEquals(response.get(0).teacherId,new Integer(2));
        //assertEquals(response.get(0).submissionDate, dateFormat.format(date));
        assertEquals(response.get(0).grade, "VG");
    }*/

    @Test
    public void getAllSubmissionsTest() {
    	String getAllSubmissionsStatement = "SELECT "
        		+ "sub.AssignmentId,sub.StudentId,stu.FirstName,stu.LastName,"
        		+ "sub.SubmissionDate,sub.Grade,sub.TeacherId FROM "
        		+ "Submission AS sub LEFT JOIN Users AS stu ON "
        		+ "sub.studentId=stu.userId WHERE (AssignmentId=?)";
    	
    	Timestamp ts = new Timestamp(System.currentTimeMillis());
    	
    	Map responseFromMock = new HashMap();
        responseFromMock.put("AssignmentId", 1);
        responseFromMock.put("StudentId", 2);
        responseFromMock.put("TeacherId", 3);
        responseFromMock.put("SubmissionDate", ts);
        responseFromMock.put("Grade", "MVG");
        responseFromMock.put("FirstName", "Hej");
        responseFromMock.put("LastName", "Sven");

        List<Map<String, Object>> listFromMock = new ArrayList<>();
        listFromMock.add(responseFromMock);
        
        when(jdbcMock.queryForList(getAllSubmissionsStatement, 1)).
                thenReturn(listFromMock);

        List<SubmissionWrapper> response = sub.getAllSubmissions("1").get();
        
        assertEquals(response.get(0).assignmentId,1);
        assertEquals(response.get(0).studentId,2);
        assertEquals(response.get(0).teacherId,new Integer(3));
        assertEquals(response.get(0).submissionDate, ts.toString());
        assertEquals(response.get(0).grade, "MVG");
        assertEquals(response.get(0).studentName, "Hej Sven");
    }
    
    @Test
    public void getAllUngradedSubmissionsTest() {
    	String getAllUngradedSubmissionsStatement = "SELECT "
        		+ "sub.AssignmentId,sub.StudentId,stu.FirstName,stu.LastName,"
        		+ "sub.SubmissionDate,sub.Grade,sub.TeacherId FROM "
        		+ "Submission AS sub LEFT JOIN Users AS stu ON "
        		+ "sub.studentId=stu.userId WHERE (AssignmentId=?) AND (Grade IS NULL)";
    	
    	Timestamp ts = new Timestamp(System.currentTimeMillis());
    	
    	Map responseFromMock = new HashMap();
        responseFromMock.put("AssignmentId", 1);
        responseFromMock.put("StudentId", 2);
        responseFromMock.put("TeacherId", null);
        responseFromMock.put("SubmissionDate", ts);
        responseFromMock.put("Grade", null);
        responseFromMock.put("FirstName", "Hej");
        responseFromMock.put("LastName", "Sven");

        List<Map<String, Object>> listFromMock = new ArrayList<>();
        listFromMock.add(responseFromMock);
        
        when(jdbcMock.queryForList(getAllUngradedSubmissionsStatement, 1)).
                thenReturn(listFromMock);

        assertTrue(sub.getAllUngraded("1").isPresent());
        
        List<SubmissionWrapper> response = sub.getAllUngraded("1").get();
        
        assertEquals(response.get(0).assignmentId,1);
        assertEquals(response.get(0).studentId,2);
        assertEquals(response.get(0).teacherId, null);
        assertEquals(response.get(0).submissionDate, ts.toString());
        assertEquals(response.get(0).grade, null);
        assertEquals(response.get(0).studentName, "Hej Sven");
    }
    
    @Test
    public void getAllSubmissionsTestWithStudents() {
    	String getAllSubmissionsWithStudentsStatement =
        		"SELECT ass.AssignmentId,par.UserId AS StudentId,sub.SubmissionDate"
        		+ ",sub.Grade,sub.TeacherId FROM Assignment AS ass RIGHT JOIN "
        		+ "Participant AS par ON ass.CourseId=par.CourseId LEFT JOIN "
        		+ "Submission AS sub ON par.userId=sub.studentId WHERE "
        		+ "(par.function='Student') AND (ass.AssignmentId=?)";
    	
    	Timestamp ts = new Timestamp(System.currentTimeMillis());
    	
    	Map responseFromMock = new HashMap();
        responseFromMock.put("AssignmentId", 1);
        responseFromMock.put("StudentId", 3);

        List<Map<String, Object>> listFromMock = new ArrayList<>();
        listFromMock.add(responseFromMock);
        
        when(jdbcMock.queryForList(getAllSubmissionsWithStudentsStatement, 1)).
                thenReturn(listFromMock);

        assertTrue(sub.getAllUngraded("1").isPresent());
        
        List<SubmissionWrapper> response = sub.getAllSubmissionsWithStudents("1").get();
        
        assertEquals(response.get(0).assignmentId,1);
        assertEquals(response.get(0).studentId,3);
        assertEquals(response.get(0).teacherId, null);
        assertEquals(response.get(0).submissionDate, null);
        assertEquals(response.get(0).grade, null);
        assertEquals(response.get(0).studentName, null);
    }
}