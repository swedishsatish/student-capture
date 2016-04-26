package studentcapture.video.videoIn;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplicationTests;

import java.io.*;

public class VideoInControllerTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Test
    public void testUploadCorrectHeaderAndEmptyBody() throws Exception {
        mockMvc.perform(post("/uploadVideo")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCorrectRequestWithWrongHeader() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File("/home/c13/c13fmg/edu/pvt/video.webm"));

        mockMvc.perform(fileUpload("/uploadVideo")
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "luddzz1337")
                .param("videoName", "video.webm")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testUploadWrongHeaderAndEmptyBody() throws Exception {
        mockMvc.perform(post("/uploadVideo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testCorrectVideoUpload() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File("/home/c13/c13fmg/edu/pvt/video.webm"));

        mockMvc.perform(fileUpload("/uploadVideo")
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "luddzz1337")
                .param("videoName", "video.webm")
                .contentType("multipart/form-data"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadWithWrongAmountOfParams() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File("/home/c13/c13fmg/edu/pvt/video.webm"));

        mockMvc.perform(fileUpload("/uploadVideo")
                .file(new MockMultipartFile("video", fileContent))
                .param("videoName", "video.webm")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithWrongParamNames() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File("/home/c13/c13fmg/edu/pvt/video.webm"));

        mockMvc.perform(fileUpload("/uploadVideo")
                .file(new MockMultipartFile("video", fileContent))
                .param("userIDddd", "luddzz1337")
                .param("videoName", "video.webm")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUploadWithWrongParamValues() throws Exception {
        byte []fileContent = FileCopyUtils.copyToByteArray(new File("/home/c13/c13fmg/edu/pvt/video.webm"));

        mockMvc.perform(fileUpload("/uploadVideo")
                .file(new MockMultipartFile("videoName", fileContent))
                .param("userID", "luddzz1337")
                .param("video", "video.webm")
                .contentType("multipart/form-data"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUploadWithEmptyVideo() throws Exception {
        byte []fileContent = {};

        mockMvc.perform(fileUpload("/uploadVideo")
                .file(new MockMultipartFile("video", fileContent))
                .param("userID", "luddzz1337")
                .param("videoName", "video.webm")
                .contentType("multipart/form-data"))
                .andExpect(status().isBadRequest());
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

}