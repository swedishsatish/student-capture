package studentcapture.datalayer.database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.*;

/**
 * Created by c12ton on 4/28/16.
 */
public class UserTest extends StudentCaptureApplicationTests {

    @Autowired
    User usr;

    @Test
    public void testAddUser() {
        boolean res = usr.addUser("Olles","heaton","persson","19890428","cheatonss");
        assertTrue(res);
    }

    @Test
    public void testUserExist() {
      //  usr.addUser("firstname","lastname","1993","paswd","userN","u" )
      //  boolean res usr.addUser()
    }

    @Test
    public void testRemoveUser() {
        //TODO insert users

    }
}