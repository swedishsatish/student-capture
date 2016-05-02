package studentcapture.datalayer.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.*;

/**
 * Created by c12ton on 4/28/16.
 */
public class UserTest extends StudentCaptureApplicationTests {


    @Autowired
    User usr;

    @Before
    public void setUp() throws Exception {

        usr = Mockito.mock(User.class);
        Mockito.when(usr.addUser("newusr","name","lname","1993","123A"))
                .thenReturn(true);

        Mockito.when(usr.userExist("oldusr","pswd123A"))
                .thenReturn(true);

        Mockito.when(usr.addUser("oldusr","name","lname","1993","123A"))
                .thenReturn(false);
    }


    @Test
    public void testAddUser() {
        assertTrue(usr.addUser("newusr","name","lname","1993","123A"));
    }


    @Test
    public void testAddExistingUser() {
        assertFalse(usr.addUser("oldusr","name","lname","1993","123A"));
    }

    @Test
    public void testUserExist() {
        assertTrue(usr.userExist("oldusr","pswd123A"));
    }

    @Test
    public void testRemoveUser() {
        //TODO insert users
    }
}