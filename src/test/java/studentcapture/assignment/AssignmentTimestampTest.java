package studentcapture.assignment;

import org.springframework.http.HttpStatus;
import studentcapture.config.StudentCaptureApplicationTests;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AssignmentTimestampTest extends StudentCaptureApplicationTests {

    @Test
    public void testIsWithinTime(){
        AssignmentTimestamp at = new AssignmentTimestamp();
        at.startAssignment("testID", "TestAssID");
        assertEquals(at.registerTime(500,0, "testID", "TestAssID"),
                HttpStatus.ACCEPTED);
    }

    @Test
    public void testIsNotWithinTime(){
        AssignmentTimestamp at = new AssignmentTimestamp();
        at.startAssignment("testID", "TestAssID");
        assertEquals(at.registerTime(-1,0, "testID", "TestAssID"),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    public void testIsNotWithinTimeAfterTime(){
        try {
            AssignmentTimestamp at = new AssignmentTimestamp();
            at.startAssignment("testID", "TestAssID");
            Thread.sleep(30);
            assertEquals(at.registerTime(2,0, "testID", "TestAssID"),
                    HttpStatus.NOT_ACCEPTABLE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsWithinTimeAfterTime(){
        try {
            AssignmentTimestamp at = new AssignmentTimestamp();
            at.startAssignment("testID", "TestAssID");
            Thread.sleep(15);
            assertEquals(at.registerTime(2000,0, "testID", "TestAssID"),
                    HttpStatus.ACCEPTED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileNotFound(){
        AssignmentTimestamp at = new AssignmentTimestamp();
        at.startAssignment("testID", "TestAssID");
        assertEquals(at.registerTime(20,0, "testID2", "TestAssID2"),
                HttpStatus.NOT_FOUND);
    }
}