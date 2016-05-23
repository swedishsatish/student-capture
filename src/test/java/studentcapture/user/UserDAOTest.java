package studentcapture.user;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.login.ErrorFlags;

import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    private User userSetup;

    @Before
    public void setup() {


        //

        //Add one user
        String sql = "INSERT INTO users"
                +" (username, firstname, lastname, email, pswd)"
                +" VALUES (?, ?, ?, ?, ?)";

        userSetup = new User("testUser","testFName","testLName",
                             "testEmail@example.com","testPassword123");

        Object[] args = new Object[] {userSetup.getUserName(), userSetup.getfName(),
                            userSetup.getlName(),userSetup.getEmail(),
                            userSetup.getPswd()};

        int[] types = new int[]{Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
                Types.VARCHAR,Types.VARCHAR};

        try {
            jdbcMock.update(sql,args,types);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Remove all content from the database
     */
    @After
    public void tearDown() {
        String sql1 = "DELETE FROM Users;";
        //Reset serialize userid
        String sql2 = "ALTER TABLE users ALTER COLUMN userid RESTART WITH 1";
        jdbcMock.update(sql1);
        jdbcMock.update(sql2);
    }

    @Test
    public void testGetUserByID() {
       // printUsersTableTemp();
        User userRes = userDAO.getUser("1",1);
        System.out.println("username:" + userRes.getUserName());
        assertEquals(userSetup,userRes);
    }


    @Test
    public void testAddUser() {

        User user = new User("userPelle","Pelle","Jönsson","pelle@gmail.com",
                            "mypassword123");

        ErrorFlags res = userDAO.addUser(user);
        User userRes = userDAO.getUser("userPelle",0);

        assertEquals(ErrorFlags.NOERROR,res);
        assertEquals(user,userRes);
    }

    @Test
    public void testAddingUserTwice() {
        ErrorFlags errorFlags = userDAO.addUser(userSetup);

        assertEquals(ErrorFlags.USEREXISTS,errorFlags);
    }

    @Test
    public void testGetNonExistingUser() {
        User userRes = userDAO.getUser("notExist",0);
        assertEquals(null,userRes);
    }

    @Test
    public void testUpdateUser() {

        userSetup.setfName("new username");
        userSetup.setPswd("new password");

        boolean res = userDAO.updateUser(userSetup);
        User userRes = userDAO.getUser("testUser",0);

        assertTrue(res);
        assertEquals(userSetup,userRes);
    }

    @Test
    public void testUpdateNonExistingUser() {
        User user = new User("testUserNotExists","newFirstName","newLname",
                "newemai@gmail.com","new_pswd_1fdsgasda213fC");
        boolean res = userDAO.updateUser(user);
        assertFalse(res);
    }

    @Test
    public void testAddingEmailTwice() {
        User user = new User("testNewUser","newFirstName","newLname",
                             userSetup.getEmail(),"newpswd");

        ErrorFlags res = userDAO.addUser(user);
        assertEquals(ErrorFlags.EMAILEXISTS,res);
    }

    @Test
    public void testAddToken() {
        userSetup.setToken("new token");

        boolean res = userDAO.updateUser(userSetup);
        User user   = userDAO.getUser(userSetup.getUserName(),0);


        assertEquals(userSetup,user);
        assertTrue(res);

    }

    @Test
    public void testRemoveToken() {
        userSetup.setToken(null);

        boolean res = userDAO.updateUser(userSetup);
        User user = userDAO.getUser(userSetup.getUserName(),0);

        assertEquals(userSetup,user);
        assertTrue(res);
    }

    /**
     * Used for debugging purposes.
     * Usage: Called inside test method.
     */
    private void printUsersTableTemp() {
        System.out.println("!!!!!!!!!!!!!!!!!TABLE!!!!!!!!!!!!!!!!!!!!!!!!!\n");

        String sql = "SELECT * FROM users";
        List<Map<String,Object>> users = jdbcMock.queryForList(sql);

        if(users != null && !users.isEmpty()) {
            for(Map<String,Object> user: users) {
                for(Iterator<Map.Entry<String, Object>> it = user.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String,Object> entry = it.next();
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key + " = " + value);
                }
                System.out.println();
            }
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

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
}
