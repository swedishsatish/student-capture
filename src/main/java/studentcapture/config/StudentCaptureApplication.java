package studentcapture.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"studentcapture.helloworld.controller"})
public class StudentCaptureApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentCaptureApplication.class, args);
	}
}
