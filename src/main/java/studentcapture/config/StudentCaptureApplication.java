package studentcapture.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan(basePackages = {"studentcapture"})
public class StudentCaptureApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentCaptureApplication.class, args);
	}

	@Bean
	public RestTemplate createTemplateMock(){
		return new RestTemplate();
	}


}
