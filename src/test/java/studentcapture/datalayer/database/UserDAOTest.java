package studentcapture.datalayer.database;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
// import studentcapture.datalayer.database.Submission.SubmissionWrapper;


import studentcapture.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.*;

/**
 * Created by tfy12hsm.
 */
public class UserDAOTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JdbcTemplate jdbcMock;

    @Before
    public void setup() {
        String sql = "INSERT INTO users"
                +" (username, firstname, lastname, email, pswd)"
                +" VALUES (?, ?, ?, ?, ?)";

        Object[] args = new Object[] {"testUser", "testFName", "testLName",
                                      "testEmail@example.com", "testPassword123"};

        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                Types.VARCHAR,Types.VARCHAR};

        jdbcMock.update(sql,args,types);

    }

    /**
     * Remove all content from the database
     */
    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Users;";
        String sql2 = "DELETE FROM Course;";
        String sql3 = "DELETE FROM Assignment;";
        String sql4 = "DELETE FROM Submission;";

        jdbcMock.update(sql4);
        jdbcMock.update(sql3);
        jdbcMock.update(sql2);
        jdbcMock.update(sql1);
    }


    @Test
    public void testAddUser() {

        User user = new User("userPelle","Pelle","Jönsson","pelle@gmail.com",
                            "mypassword123");

       boolean res = userDAO.addUser(user);

        //Getting values from table user
        String sql = "SELECT * FROM users WHERE username = 'userPelle'";
        User dbUser = (User) jdbcMock.queryForObject(sql, new UserWrapper());

        assertEquals("userPelle",dbUser.getUserName());
        assertEquals("Pelle",dbUser.getfName());
        assertEquals("Jönsson",dbUser.getlName());
        assertEquals("pelle@gmail.com",dbUser.getEmail());
        assertEquals("mypassword123",dbUser.getPswd());

        assertTrue(res);
    }

    @Test
    public void testGetPswd() {
        assertEquals("testPassword123",userDAO.getPswd("testUser"));
    }

    @Test
    public void testGetPswdForNonExistingUser() {
        assertEquals(null,userDAO.getPswd("testUser1"));
    }

    @Test
    public void testEmailExist() {
        assertTrue(userDAO.emailExist("testEmail@example.com"));
    }

    @Test
    public void testEmailDoesNotExist() {
        assertFalse(userDAO.emailExist("testEmail2@example.com"));
    }

    @Test
    public void testUserExist() {
        assertTrue(userDAO.userNameExist("testUser"));
    }

    @Test
    public void testUserDoesNotExist() {
        assertFalse(userDAO.userNameExist("test321321321321User"));
    }


    @Test
    public void testChangePswdHasChanged() {
        boolean res = userDAO.changePswd("testUser","newpswd");

        //Getting values from table user
        String sql = "SELECT * FROM users WHERE username = 'testUser'";
        User dbUser = (User) jdbcMock.queryForObject(sql, new UserWrapper());

        assertTrue(res);
        assertEquals("newpswd",dbUser.getPswd());
    }

    @Test
    public void testChangeNonExistingUserPswd() {
        boolean res = userDAO.changePswd("testUserNotExist","pswd");
        assertFalse(res);
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
            User user = new User(rs.getString("username"),rs.getString("firstname"),
                    rs.getString("lastname"),rs.getString("email"),
                    rs.getString("pswd"));
            return user;
        }
    }

}
