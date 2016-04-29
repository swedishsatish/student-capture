package studentcapture.lti;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import studentcapture.feedback.FeedbackModel;

import static org.junit.Assert.*;

/**
 * Will test the LTICommunicator
 * @author ltiChapter
 */
public class LTICommunicatorTest {

    FeedbackModel fm;
    @Before
    public void setUp() throws Exception {
        fm = new FeedbackModel();
        fm.setGrade("0.1");
        fm.setFeedbackText("Dåligt");
        fm.setAssignmentID(100);
        fm.setCourseID(2000);
        fm.setStudentID(12);
    }

    @After
    public void tearDown() throws Exception {
        fm = null;
    }

    @Test
    public void setGrade() throws Exception {

    }
}