package studentcapture.datalayer.database;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import studentcapture.config.StudentCaptureApplicationTests;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by S&E on 4/27/16.
 */
public class AssignmentTest extends StudentCaptureApplicationTests {

    @Autowired
    Assignment ass;
    ArrayList actual;

    /*@Test
    public void testCreateAssignment() throws Exception {

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
        expected.add("2016-01-01 12:12:12");
        expected.add("2016-01-01 12:12:13");
        expected.add("1");
        expected.add("2");

        actual = ass.getAssignmentInfo(10);

        assertEquals(expected, actual);
    }

    /*@Test
    public void testUpdateAssignment() throws Exception {

    }

    @Test
    public void testRemoveAssignment() throws Exception {

    }*/
}