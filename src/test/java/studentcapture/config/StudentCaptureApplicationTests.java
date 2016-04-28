package studentcapture.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StudentCaptureApplication.class)
@WebAppConfiguration
@Configuration
public class StudentCaptureApplicationTests {

	@Bean
	@Primary
	public RestTemplate restTemplateMock() {
		return Mockito.mock(RestTemplate.class);
	}


	@Test
	public void contextLoads() {
	}
}
