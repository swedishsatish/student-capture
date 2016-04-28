package studentcapture.video.videoIn;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RequestManagerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private MockHttpServletRequest mockHttp;
    @Test
    public void testRequestPOSTVideo() throws Exception {

    }

    @Test
    public void testRequestGETVideo() throws Exception {

    }

    @Test
    public void testRequestPostTestVideo() throws Exception {
        mockHttp.addParameter("videoName", "kuken");
        mockHttp.addParameter("videoTest", "hejsan");
        //mockHttp.
    }

    @Test
    public void testHashCodeGenerator() throws Exception {

    }
}