package studentcapture.feedback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests the FeedbackModel to see that getters and setters works as intended.
 */
public class FeedbackModelTest {
    FeedbackModel fm;
    @Before
    public void setUp() throws Exception {
        fm = new FeedbackModel();
        fm.setAssignmentID(100);
        fm.setFeedbackText("Mycket bra");
        fm.setGrade("2.32");
        fm.setStudentID(349);
    }

    @After
    public void tearDown() throws Exception {
        fm = null;
    }

    @Test
    public void shouldReturnCorrectUserID() throws Exception {
        assertEquals(349, fm.getStudentID());
    }
    @Test
    public void shouldNotReturnSameUserID() throws Exception {
        assertNotEquals(1, fm.getStudentID());
    }

    @Test
    public void shouldReturnCorrectAssignmentId() throws Exception {
        assertEquals(100, fm.getAssignmentID());
    }

    @Test
    public void shouldNotReturnSameAssignmentId() throws Exception {
        assertNotEquals(1, fm.getAssignmentID());
    }

    @Test
    public void shouldReturnCorrectGrade() throws Exception {
        assertEquals("2.32", fm.getGrade());
    }

    @Test
    public void shouldNotReturnSameGrade() throws Exception {
        assertNotEquals("2.2", fm.getGrade());
    }


    @Test
    public void shouldReturnCorrectFeedbackText() throws Exception {
        assertEquals("Mycket bra", fm.getFeedbackText());
    }

    @Test
    public void shouldNotReturnSameFeedbackText() throws Exception {
        assertNotEquals("Mycket dåligt", fm.getFeedbackText());
    }
}