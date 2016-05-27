package studentcapture.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

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

	@Bean
	@Primary
	public JdbcTemplate jdbcTemplateMock() {

		return new JdbcTemplate(H2DB.dataSource());
	}

	public final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(),
			Charset.forName("utf8")
	);

	@Test
	public void contextLoads(){

	}

}
