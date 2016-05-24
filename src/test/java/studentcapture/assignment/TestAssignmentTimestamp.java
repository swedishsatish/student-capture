package studentcapture.assignment;

import org.springframework.http.HttpStatus;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.assertEquals;



/**
 * Tests for AssignmentTimestamp.
 * Created by dv14sen on 2016-05-16.
 */
public class TestAssignmentTimestamp extends StudentCaptureApplicationTests {


    @org.junit.Test
    public void testIsWithinTime(){
        AssignmentTimestamp at = new AssignmentTimestamp();
        at.startAssignment("testID", "TestAssID");
        assertEquals(at.registerTime(500,0, "testID", "TestAssID"), HttpStatus.ACCEPTED);
    }
    @org.junit.Test
    public void testIsNotWithinTime(){
        AssignmentTimestamp at = new AssignmentTimestamp();
        at.startAssignment("testID", "TestAssID");
        assertEquals(at.registerTime(-1,0, "testID", "TestAssID"), HttpStatus.NOT_ACCEPTABLE);
    }
    @org.junit.Test
    public void testIsNotWithinTimeAfterTime(){
        try {
            AssignmentTimestamp at = new AssignmentTimestamp();
            at.startAssignment("testID", "TestAssID");
            Thread.sleep(30);
            assertEquals(at.registerTime(2,0, "testID", "TestAssID"), HttpStatus.NOT_ACCEPTABLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @org.junit.Test
    public void testIsWithinTimeAfterTime(){
        try {
            AssignmentTimestamp at = new AssignmentTimestamp();
            at.startAssignment("testID", "TestAssID");
            Thread.sleep(15);
            assertEquals(at.registerTime(2000,0, "testID", "TestAssID"), HttpStatus.ACCEPTED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @org.junit.Test
    public void testFileNotFound(){

        AssignmentTimestamp at = new AssignmentTimestamp();
        at.startAssignment("testID", "TestAssID");
        assertEquals(at.registerTime(20,0, "testID2", "TestAssID2"), HttpStatus.NOT_FOUND);

    }
}