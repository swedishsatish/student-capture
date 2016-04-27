package studentcapture.feedback;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class:       BasicSanitizerTest
 * <p>
 * Author:      Erik Moström
 * cs-user:     erikm
 * Date:        4/26/16
 */
public class BasicSanitizerTest {
    private Sanitizer s;

    @Before
    public void setUp(){
        s = new BasicSanitizer();
    }

    @Test
    public void shouldReturnSameStringWhenCorrect() throws Exception {
        assertEquals("abcAZ", s.sanitize("abcAZ"));
    }

    @Test
    public void shouldReturnCleanedString() throws Exception {
        assertEquals("hej", s.sanitize("hej!"));
    }
}