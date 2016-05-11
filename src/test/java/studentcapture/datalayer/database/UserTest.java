package studentcapture.datalayer.database;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.datalayer.database.SubmissionDAO.SubmissionWrapper;
import studentcapture.datalayer.database.User.CourseAssignmentHierarchy;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by tfy12hsm.
 */
public class UserTest  extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    User user;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Before
    public void setUp() {
        Mockito.reset(jdbcMock);
    }

    @Test
    public void getCourseAssignmentHierarchyUserInformationTest() {
    	String getUserStatement = "SELECT * FROM Users WHERE "
        		+ "UserId=?";
    	String getTeacherHierarchyStatement = "SELECT * FROM Participant AS par"
				+ " LEFT JOIN Course AS cou ON par.courseId="
	    		+ "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
	    		+ "ass.courseId LEFT JOIN Submission AS sub ON "
	    		+ "ass.assignmentId=sub.assignmentId WHERE par.userId=? AND "
	    		+ "par.function='Teacher'";
    	String getStudentHierarchyStatement = "SELECT * FROM "
	    		+ "Participant AS par LEFT JOIN Course AS cou ON par.courseId="
	    		+ "cou.courseId LEFT JOIN Assignment AS ass ON cou.courseId="
	    		+ "ass.courseId LEFT JOIN Submission AS sub ON par.userId="
	    		+ "sub.studentId AND ass.assignmentId=sub.assignmentId WHERE "
	    		+ "par.userId=? AND par.function='Student'";

    	Timestamp ts = new Timestamp(System.currentTimeMillis());

    	Map responseFromMockUser = new HashMap();
    	responseFromMockUser.put("UserId", 1);
    	responseFromMockUser.put("FirstName", "nameFirst");
    	responseFromMockUser.put("LastName", "nameLast");
    	when(jdbcMock.queryForMap(getUserStatement, 1)).
        		thenReturn(responseFromMockUser);

    	Map responseFromMockStudent = new HashMap();
    	List<Map<String, Object>> listFromMockStudent = new ArrayList<>();
    	listFromMockStudent.add(responseFromMockStudent);
    	when(jdbcMock.queryForList(getStudentHierarchyStatement, 1)).
			thenReturn(listFromMockStudent);

    	Map responseFromMockTeacher = new HashMap();
    	List<Map<String, Object>> listFromMockTeacher = new ArrayList<>();
    	listFromMockStudent.add(responseFromMockTeacher);
    	when(jdbcMock.queryForList(getTeacherHierarchyStatement, 1)).
			thenReturn(listFromMockTeacher);

    	CourseAssignmentHierarchy result =
    			user.getCourseAssignmentHierarchy("1").get();

    	assertEquals(result.userId,1);
        assertEquals(result.firstName,"nameFirst");
        assertEquals(result.lastName,"nameLast");
    }
}
