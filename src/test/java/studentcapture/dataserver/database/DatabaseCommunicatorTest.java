package studentcapture.dataserver.database;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by c13gan on 2016-04-26.
 */
public class DatabaseCommunicatorTest {

    DatabaseCommunicator dlc = new DatabaseCommunicator();
    @Test
    public void testReturnGrade() throws Exception {
        //dlc.insertTestValues();
        assertEquals("vg",dlc.returnGrade(1337, 10));
    }
}