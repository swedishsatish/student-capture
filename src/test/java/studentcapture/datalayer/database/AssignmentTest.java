package studentcapture.datalayer.database;

import org.junit.Before;
import studentcapture.config.StudentCaptureApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import studentcapture.config.StudentCaptureApplicationTests;
//import sun.jvm.hotspot.utilities.Assert;

import java.util.ArrayList;

import static org.junit.Assert.*;


/**
 * Created by S&E on 4/27/16.
 */
public class AssignmentTest extends StudentCaptureApplicationTests {


    @Autowired
    Assignment ass;
    ArrayList actual;

    @Test
    public void testCreateAssignment() throws Exception {
        String courseID = "qwe";
        String assignmentTitle = "PVT";
        String startDate = "2016-10-19 11:12:12+02";
        String endDate = "2016-10-19 11:14:12+02";
        String minTime = "180";
        String maxTime = "360";
        boolean published = false;

        int assID = ass.createAssignment(courseID, assignmentTitle, startDate, endDate, minTime, maxTime, published);

        System.out.println(assID + "awdawd");

        assertNotNull(assID);
    }


    /*
    @org.junit.Test
    public void testGetAssignmentInfo() throws Exception {

    }*/

    @Test
    public void testGetAssignmentReturnListOfCorrectLength() throws Exception {
        actual = ass.getAssignmentInfo(10);

        assertEquals(6, actual.size());
    }

    @Test
    public void testGetAssignmentReturnsNotNUll() {


        actual = ass.getAssignmentInfo(10);

        assertFalse(actual.contains(null));
    }

    @Test
    public void testGetAssignmentReturnsCorrectInfo() {
        ArrayList expected = new ArrayList<String>();
        expected.add("enkurs");
        expected.add("titel");
        expected.add("datum");
        expected.add("datum2");
        expected.add("tid");
        expected.add("tid3");

        actual = ass.getAssignmentInfo(10);

        //assertEquals(actual, expected);
    }

    /*@Test
    public void testUpdateAssignment() throws Exception {

    }

    @Test
    public void testRemoveAssignment() throws Exception {

    }*/
}