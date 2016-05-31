package studentcapture.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.H2DB;
import studentcapture.config.StudentCaptureApplicationTests;
import studentcapture.login.ErrorFlags;

import java.sql.SQLException;
import java.sql.Types;

import static org.junit.Assert.*;

/**
 * 
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

        //Add one user
        String sql = "INSERT INTO users"
                +" (username, firstname, lastname, email, pswd)"
                +" VALUES (?, ?, ?, ?, ?)";

        userSetup = new User("testUser","testFName","testLName",
                             "testEmail@example.com","testPassword123", false);

        Object[] args = new Object[] {userSetup.getUserName(), userSetup.getFirstName(),
                            userSetup.getLastName(),userSetup.getEmail(),
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
        try {
            H2DB.TearDownDB(jdbcMock);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserByID() {
        User userRes = userDAO.getUser("1",1);
        assertEquals(userSetup,userRes);
    }


    @Test
    public void testAddUser() {

        User user = new User("userPelle", "Pelle", "Jönsson", "pelle@gmail.com",
                "mypassword123", false);

        ErrorFlags res = userDAO.addUser(user);
        User userRes = userDAO.getUser("userPelle", UserDAO.GET_USER_BY_USERNAME);

        assertEquals(ErrorFlags.NOERROR, res);
        assertEquals(user, userRes);
    }

    @Test
    public void testAddingUserTwice() {
        H2DB.printTable(jdbcMock,"users");

        ErrorFlags errorFlags = userDAO.addUser(userSetup);
        H2DB.printTable(jdbcMock,"users");
        assertEquals(ErrorFlags.USEREXISTS,errorFlags);

    }

    @Test
    public void testAddingNullUser() {
        User user = new User("userPelle","Pelle","Jönsson",null,
                             "mypassword123", false);

        ErrorFlags errorFlag = userDAO.addUser(user);
        assertEquals(ErrorFlags.USERCONTAINNULL, errorFlag);
    }

    @Test
    public void testGetNonExistingUser() {
        User userRes = userDAO.getUser("notExist",0);
        assertEquals(null,userRes);
    }

    @Test
    public void testUpdateUser() {

        userSetup.setFirstName("new username");
        userSetup.setPswd("new password");

        boolean res = userDAO.updateUser(userSetup);
        User userRes = userDAO.getUser("testUser",0);

        assertTrue(res);
        assertEquals(userSetup,userRes);
    }

    @Test
    public void testUpdateNonExistingUser() {
        User user = new User("testUserNotExists","newFirstName","newLname",
                "newemai@gmail.com","new_pswd_1fdsgasda213fC", false);
        boolean res = userDAO.updateUser(user);
        assertFalse(res);
    }

    @Test
    public void testAddingEmailTwice() {
        User user = new User("testNewUser","newFirstName","newLname",
                             userSetup.getEmail(),"newpswd", false);

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
    
    @Test
    public void testIsTeacher() {
    	
    	User user = new User("userPelle", "Pelle", "Jönsson", "pelle@gmail.com",
                "mypassword123", true);
    	
    	ErrorFlags result = userDAO.addUser(user);
    	user = userDAO.getUser(user.getUserName(), UserDAO.GET_USER_BY_USERNAME);
    	
    	assertTrue(user.isTeacher());
    }
}
