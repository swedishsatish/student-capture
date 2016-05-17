package studentcapture.usersettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import studentcapture.config.StudentCaptureApplication;
import studentcapture.config.StudentCaptureApplicationTests;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by andreassavva on 2016-05-12.
 */
public class UserSettingsResourceTest extends StudentCaptureApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestTemplate templateMock;

    private MockMvc mockMvc;

    @Test
    public void testCorrectSettingsInput() throws Exception {

        if (mockMvc == null) {
            System.err.println("NULL");
        }

        ResponseEntity<String> response = new ResponseEntity<String>(HttpStatus.OK);
        when(
                templateMock.postForEntity
                        (any(String.class),
                                any(LinkedMultiValueMap.class),
                                eq(String.class)))
                .thenReturn(response);

        mockMvc.perform(post("/settings")
                .param("userID", "user")
                .param("language", "english")
                .param("emailAddress", "c13swn@cs.umu.se")
                .param("textSize", "12")
                .param("newUser", "true"))
                .andExpect(status().isOk());
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Mockito.reset(templateMock);
    }

}
