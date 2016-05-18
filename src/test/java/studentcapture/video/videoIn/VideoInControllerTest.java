package studentcapture.video.videoIn;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VideoInControllerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate templateMock;

    private MockMvc mockMvc;
    private String userID = "26";

    private byte[] fileContent;

    @Test
    public void testUploadCorrectHeaderAndEmptyBody() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(post("/uploadVideo/" + Integer.toString(userID.hashCode()))
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWithWrongHeader() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("bugsbunny", fileContent))
                .param("userID", userID)
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testUploadWrongHeaderAndEmptyBody() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(post("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testCorrectVideoUpload() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", userID)
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCorrectVideoUploadButFileTransferFails() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("Failed");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", userID)
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorrectVideoUploadButFileTransferFails2() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("Server error");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", userID)
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorrectVideoUploadButFileTransferResponseReturnsNull() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn(null);

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", userID)
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithWrongAmountOfParams() throws Exception {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", userID)
                .param("courseID", "5DV151")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithWrongParamNames() throws Exception {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("videoo", fileContent))
                .param("userIDD", userID)
                .param("assignmentIDD", "1337")
                .param("courseIDD", "5DV151")
                .param("courseCodee", "5DV151")
                .param("videoTypee", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithWrongParamValues() throws Exception {
        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("videoName", fileContent))
                .param("userID", "user123123213123")
                .param("assignmentID", "1337")
                .param("courseID", "5DV15111111")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithEmptyVideo() throws Exception {
        byte[] emptyVideoFile = {};

        when(templateMock.postForObject(any(String.class), any(LinkedMultiValueMap.class), any())).thenReturn("OK");

        mockMvc.perform(fileUpload("/uploadVideo/"+Integer.toString(userID.hashCode()))
                .file(new MockMultipartFile("video", emptyVideoFile))
                .param("userID", userID)
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("courseCode", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Before
    public void setUp() throws Exception {
        fileContent = FileCopyUtils.copyToByteArray(
                new File(StudentCaptureApplication.ROOT + "/bugsbunny.webm"));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        Mockito.reset(templateMock);
        try {
            mockMvc.perform(get("/video/inrequest")
                            .param("userID", userID)
                            .param("courseID", "5DV151")
                            .param("assignmentID", "1337")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}