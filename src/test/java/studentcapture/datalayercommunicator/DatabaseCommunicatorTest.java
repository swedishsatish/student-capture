package studentcapture.datalayercommunicator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by c13gan on 2016-04-26.
 */
public class DatabaseCommunicatorTest {

    DatabaseCommunicator dlc = new DatabaseCommunicator();
    @Test
    public void shouldReturnGrade() throws Exception {
        //dlc.insertTestValues();
        assertEquals("vg",dlc.returnGrade(1337, 10));
    }

    @Test
    public void shouldReturnMissingGradeNoAssignment() throws Exception {
        assertEquals("Missing grade", dlc.returnGrade(1337, 5));
    }


    @Test
    public void shouldReturnMissingGradeNoUser() throws Exception {
        assertEquals("Missing grade", dlc.returnGrade(5, 10));
    }


    @Test
    public void shouldReturnMissingGrade() throws Exception {
        assertEquals("Missing grade", dlc.returnGrade(5, 5));
    }

}