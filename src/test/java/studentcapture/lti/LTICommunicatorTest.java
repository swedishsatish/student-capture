package studentcapture.lti;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import studentcapture.model.Grade;
import studentcapture.submission.Submission;

/**
 * Will test the LTICommunicator
 * @author ltiChapter
 */
public class LTICommunicatorTest {

    Submission submission;
    @Before
    public void setUp() throws Exception {
        submission = new Submission();
        submission.setGrade(new Grade("VG", 1));
        submission.setFeedback("Dåligt");
        submission.setAssignmentID(100);
        submission.setCourseID(2000);
        submission.setStudentID(12);
    }

    @After
    public void tearDown() throws Exception {
        submission = null;
    }

    @Test
    public void setGrade() throws Exception {

    }
}