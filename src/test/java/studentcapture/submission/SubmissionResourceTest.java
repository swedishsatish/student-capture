package studentcapture.submission;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Configuration
public class SubmissionResourceTest extends StudentCaptureApplicationTests{
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private SubmissionDAO submissionDAO;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        reset(submissionDAO);

    }

    private Submission createSubmission(int studentID, int assignmentID) {
        Submission submission = new Submission();
        submission.setStudentID(studentID);
        submission.setAssignmentID(assignmentID);
        return submission;
    }

    @Test
    public void shouldReturnEmptyIfNoSubmissions() throws Exception {
        int assignmentID = 1;
        when(submissionDAO.getAllSubmissions(assignmentID)).thenReturn(new ArrayList<Submission>());
        mvc.perform(get("/assignments/" + assignmentID + "/submissions/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnSubmissions() throws Exception {
        int assignmentID = 1;
        int count = 5;
        ArrayList<Submission> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(createSubmission(i, i));
        }
        when(submissionDAO.getAllSubmissions(assignmentID)).thenReturn(list);
        mvc.perform(get("/assignments/" + assignmentID + "/submissions/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(count)))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldReturnServerErrorIfDatabaseError() throws Exception {
        int assignmentID = 1;
        when(submissionDAO.getAllSubmissions(assignmentID)).thenReturn(null);
        mvc.perform(get("/assignments/" + assignmentID + "/submissions/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldReturnNotFoundIfSubmissionIsMissing() throws Exception {
        int assignmentID = 1;
        int studentID = 1;
        when(submissionDAO.getSubmission(assignmentID, studentID)).thenReturn(Optional.empty());
        mvc.perform(get("/assignments/" + assignmentID + "/submissions/" + studentID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void shouldReturnSubmission() throws Exception {
        int assignmentID = 1;
        int studentID = 1;
        Submission submission = createSubmission(studentID, assignmentID);
        when(submissionDAO.getSubmission(assignmentID, studentID)).thenReturn(Optional.of(submission));
        mvc.perform(get("/assignments/" + assignmentID + "/submissions/" + studentID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.assignmentID", is(assignmentID)))
                .andExpect(jsonPath("$.studentID", is(studentID)))
                .andExpect(status().isOk());

    }
}