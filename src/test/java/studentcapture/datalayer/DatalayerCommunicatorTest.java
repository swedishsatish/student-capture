package studentcapture.datalayer;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.*;

/**
 * Created by c13gan on 2016-04-26.
 */
public class DatalayerCommunicatorTest extends StudentCaptureApplicationTests{

    @Autowired
    DatalayerCommunicator dlc;
    @Test
    public void testTest1() throws Exception {
        assertEquals("vg",dlc.test());
    }
}