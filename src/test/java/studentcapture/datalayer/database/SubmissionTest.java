package studentcapture.datalayer.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import studentcapture.config.StudentCaptureApplicationTests;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by c13gan on 2016-04-27.
 */
public class SubmissionTest extends StudentCaptureApplicationTests {

    @Autowired
    Submission dlc;
    @Test
    public void shouldReturnGrade() throws Exception {
        ArrayList test = new ArrayList<Object>();
        test = dlc.getGrade("1337", "10");
        assertEquals("vg",(String)test.get(0));
    }

    /*@Test
    public void shouldReturnNoGradeWhenAssignmentNotExists() throws Exception {
        assertEquals("No submission for this user ID and/or assignment ID", dlc.returnGrade(1337, 5));
    }


    @Test
    public void shouldReturnNoGradeUserNotExists() throws Exception {
        assertEquals("No submission for this user ID and/or assignment ID", dlc.returnGrade(5, 10));
    }


    @Test
    public void shouldReturnNoGradeUserAndAssignmentNotExists() throws Exception {
        assertEquals("No submission for this user ID and/or assignment ID", dlc.returnGrade(5, 5));
    }

    @Test
    public void shouldReturnMissingGrade() throws Exception {
        assertEquals("Missing grade", dlc.returnGrade(1337, 11));
    }*/

}



