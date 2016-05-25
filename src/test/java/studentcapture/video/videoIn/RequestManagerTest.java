package studentcapture.video.videoIn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.config.StudentCaptureApplicationTests;

import java.io.File;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by c13vfm on 2016-05-03.
 */
public class RequestManagerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate templateMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        Mockito.reset(templateMock);

    }

    @Test
    public void testRequestPOSTVideo() throws Exception {

    }

    @Test
    public void testRequestGETVideo() throws Exception {

    }


    @Test
    public void testUserAllowedToUpload() throws Exception {
        String userID = "user";
        String courseID = "5DV151";
        String assignmentID = "1337";


        String temporary = mockMvc.perform(get("/video/inrequest")
                .param("userID", userID)
                .param("courseID", courseID)
                .param("assignmentID", assignmentID)
                .contentType("multipart/form-data"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //assertTrue(Boolean.parseBoolean(temporary));

    }


    @Test
    public void shouldGiveBackOkOnReqTestVidtest() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        String temporary = mockMvc.perform(fileUpload("/video/textTest")
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "user")
                .contentType("multipart/form-data"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(Base64.getEncoder().encodeToString(fileContent), temporary);
    }
}