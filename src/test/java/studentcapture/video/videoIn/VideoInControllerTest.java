package studentcapture.video.videoIn;

import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.config.StudentCaptureApplicationTests;

import static org.mockito.Mockito.when;
import static org.mockito.Matchers.*;

import java.io.*;

public class VideoInControllerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate templateMock;

    private MockMvc mockMvc;
    private String id = "-1129137218";

    @Test
    public void testUploadCorrectHeaderAndEmptyBody() throws Exception {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(post("/uploadVideo/"+id)
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadBadID() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id+"asdsad")
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "user")
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testWithWrongHeader() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("bugsbunny", fileContent))
                .param("userID", "user")
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testUploadWrongHeaderAndEmptyBody() throws Exception {
        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(post("/uploadVideo/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testCorrectSubmissionUpload() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("studentID", "c13asa")
                .param("userID", "user")
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCorrectAssignmentUpload() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("endDate", "2016-12-03T12:15:00")
                .param("userID", "user")
                .param("title", "Question Video")
                .param("courseID", "5DV151")
                .param("videoType", "assignment")
                .param("startDate", "2016-06-03T10:15:00")
                .param("minTime", "3")
                .param("maxTime", "15")
                .param("published", "True")
                .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCorrectFeedbackUpload() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .file(new MockMultipartFile("feedbackText", fileContent))
                .param("studentID", "c13asa")
                .param("userID", "user")
                .param("assignmentID", "1337")
                .param("videoType", "feedback")
                .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCorrectSubmissionUploadButNullValues() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        String temp = null;

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("studentID", temp)
                .param("userID", "user")
                .param("assignmentID", temp)
                .param("courseID", temp)
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorrectAssignmentUploadButNullValues() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        String temp = null;

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("endDate", temp)
                .param("userID", "user")
                .param("title", temp)
                .param("courseID", temp)
                .param("videoType", "assignment")
                .param("startDate", temp)
                .param("minTime", temp)
                .param("maxTime", temp)
                .param("published", temp)
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorrectFeedbackUploadButNullValues() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        String temp = null;
        byte[] tempFile = null;

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .file(new MockMultipartFile("feedbackText", tempFile))
                .param("studentID", temp)
                .param("userID", "user")
                .param("assignmentID", temp)
                .param("videoType", "feedback")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorrectVideoUploadButFileTransferFails() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "user")
                .param("studentID", "c13asa")
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCorrectVideoUploadButFileTransferFails2() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "user")
                .param("studentID", "c13asa")
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testCorrectVideoUploadButFileTransferResponseReturnsNull() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "user")
                .param("studentID", "c13asa")
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUploadWithWrongAmountOfParams() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT + "/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "user")
                .param("studentID", "c13asa")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithWrongParamNames() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("videoo", fileContent))
                .param("userIDD", "user")
                .param("studentIDD", "c13asa")
                .param("assignmentIDD", "1337")
                .param("courseIDD", "5DV151")
                .param("videoTypee", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithWrongParamValues() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File(StudentCaptureApplication.ROOT+"/bugsbunny.webm"));

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("videoName", fileContent))
                .param("userID", "user123123213123")
                .param("studentID", "123123123")
                .param("assignmentID", "1337")
                .param("courseID", "5DV15111111")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithEmptyVideo() throws Exception {
        byte[] fileContent = {};

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(templateMock.postForEntity(any(String.class), any(LinkedMultiValueMap.class), eq(String.class))).thenReturn(response);

        mockMvc.perform(fileUpload("/uploadVideo/"+id)
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "user")
                .param("studentID", "c13asa")
                .param("assignmentID", "1337")
                .param("courseID", "5DV151")
                .param("videoType", "submission")
                .contentType("multipart/form-data"))
                .andExpect(status().isNoContent());
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
        Mockito.reset(templateMock);
        try {
            mockMvc.perform(get("/video/inrequest")
                    .param("userID", "user")
                    .param("courseID", "5DV151")
                    .param("assignmentID", "1337")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}