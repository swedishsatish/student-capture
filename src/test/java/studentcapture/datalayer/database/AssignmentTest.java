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

    /*@Test
    public void testUpdateAssignment() throws Exception {

    }

    @Test
    public void testRemoveAssignment() throws Exception {

    }*/
}
