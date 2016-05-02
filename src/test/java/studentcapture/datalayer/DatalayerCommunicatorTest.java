package studentcapture.datalayer;

import java.util.List;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import studentcapture.config.StudentCaptureApplicationTests;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by oskaralfsson on 2016-04-28.
 */
public class DatalayerCommunicatorTest extends StudentCaptureApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @org.junit.Test
    public void testCreateAssignment() throws Exception {
        MvcResult result = mockMvc.perform(post("/DB/createAssignment").param("courseID", "5dv123").param("assignmentTitle", "IntroTillDB")
                .param("startDate", "2016-04-27 15:23:00").param("endDate", "2016-04-27 18:23:00")
                .param("minTime", "180").param("maxTime", "360").param("published", "false"))
                .andExpect(status().isOk())
                .andReturn();

        String resStr = result.getResponse().getContentAsString();

        // Throws exception if fail
        assertEquals(Integer.parseInt(resStr), 1234);
    }

    @org.junit.Test
    public void setGradeCorrectValues() throws Exception {
	MvcResult result = mockMvc.perform(post("/DB/setGrade").param("assID", "12").param("teacherID", "abc1234")
                .param("studentID", "xyzw4321").param("grade", "g"))
                .andExpect(status().isOk())
                .andReturn();
        String resStr = result.getResponse().getContentAsString();
        System.out.println(resStr);
	assertTrue(result.equals(true));
    }

    @org.junit.Test
    public void setGradeIncorrectValues() throws Exception {
        MvcResult result = mockMvc.perform(post("/DB/setGrade").param("assID", null).param("teacherID", "abc1234")
                .param("studentID", "xyzw4321").param("grade", "g"))
                .andExpect(status().isOk())
                .andReturn();
        assertTrue(result.equals(true));
    }
    
    @org.junit.Test
    public void getAllSubmissionsTest() throws Exception {
    	MvcResult result = mockMvc.perform(post("/DB/getAllSubmissions")
    			.param("assignmentID", "1000")).andExpect(status().isOk())
    			.andReturn();
    	
    	assertTrue(result.getResponse().getContentAsString().startsWith("[{\"assignmentId\":1000,\"studentId\":"));
    }
}
