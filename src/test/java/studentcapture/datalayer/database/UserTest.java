package studentcapture.datalayer.database;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
// import studentcapture.datalayer.database.Submission.SubmissionWrapper;
import studentcapture.datalayer.database.User.CourseAssignmentHierarchy;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Test
    public void testAddUser() {

       boolean res =  user.addUser("userPelle","Pelle","Jönsson","1944","mypassword123");

        String sql = "SELECT * FROM users WHERE username = 'userPelle'";

        //Getting values from table user
        HashMap<String,String> info = (HashMap<String,String>)
                jdbcMock.queryForObject(sql, new UserWrapper());

        assertEquals("userPelle",info.get("username"));
        assertEquals("Pelle",info.get("fName"));
        assertEquals("Jönsson",info.get("lName"));
        assertEquals("1944",info.get("SNN"));
    }

    /*
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
    	when(jdbcMock.queryForList(getUserStatement, 1)).
			thenReturn(listFromMockStudent);

    	Map responseFromMockTeacher = new HashMap();
    	List<Map<String, Object>> listFromMockTeacher = new ArrayList<>();
    	listFromMockStudent.add(responseFromMockTeacher);
    	when(jdbcMock.queryForList(getUserStatement, 1)).
			thenReturn(listFromMockTeacher);

    	CourseAssignmentHierarchy result =
    			user.getCourseAssignmentHierarchy("1").get();

    	assertEquals(result.userId,1);
        assertEquals(result.firstName,"nameFirst");
        assertEquals(result.lastName,"nameLast");
    }
	*/

    /**
     *  Used to collect user information, and return a hashmap.
     */
    protected class UserWrapper implements org.springframework.jdbc.core.RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int i) throws SQLException {
            HashMap<String,String> info = new HashMap();
            info.put("userID",rs.getString("userid"));
            info.put("username",rs.getString("UserName"));
            info.put("fName",rs.getString("FirstName"));
            info.put("lName",rs.getString("LastName"));
            info.put("SNN",rs.getString("persnr"));
            info.put("pswd",rs.getString("pswd"));
            return info;
        }
    }

}
