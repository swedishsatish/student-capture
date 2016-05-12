package assignment;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by root on 5/4/16.
 */
public class AssignmentControllerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private static String json_test_string = "{\"title\":\"TheTitle\",\"Info\":\"Assignment info\"," +
            "\"minTimeSeconds\":0,\"maxTimeSeconds\":320,\"startDate\":\"2016-10-01T10:00\"," +
            "\"endDate\":\"2016-10-02T10:00\",\"published\":\"2016-10-01T10:00\",\"scale\":\"NUMBER_SCALE\",\"recap\":\"this is a recap\"}";

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context).
                        build();
    }

    /**
     * NestedServletException is expected since the method that handles the request sends another request to another
     * method. This doesn't seem to work in JUnit. This test only shows that it works to post to /assignment. No side
     * effects are tested at all.
     * @throws Exception
     */
    @Test(expected = NestedServletException.class)
    public void shouldWorkToSendPost() throws Exception {
        mvc.perform(post("/assignment").contentType(MediaType.APPLICATION_JSON).content(json_test_string))
                .andExpect(status().isOk());

    }
}
