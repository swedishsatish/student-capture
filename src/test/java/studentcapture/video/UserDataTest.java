package studentcapture.video;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by c13bll on 2016-04-26.
 */
public class UserDataTest {
    UserData ud;

    @Before
    public void setUp() throws Exception {
        ud = new UserData();
        ud.addCourseID((long)123123).addUserID("Bjorn").addExamID("5DV151");
    }

    @After
    public void tearDown() throws Exception {
        ud = null;
    }

    @Test
    public void testCourseIdNotNull(){
        assertNotNull(ud.getCourseID());
    }

    @Test
    public void testuserIDNotNull(){
        assertNotNull(ud.getUserID());
    }

    @Test
    public void testGetuserID(){
        assertEquals("Bjorn",ud.getUserID());

    }

    @Test
    public void testGetcourseID(){
        assertEquals(Long.valueOf("123123"),ud.getCourseID());
    }

    @Test
    public void testExamId(){
        assertEquals("5DV151",ud.getExamID());
    }
}