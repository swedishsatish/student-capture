package studentcapture.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"studentcapture"})
public class StudentCaptureApplication {
	public static String ROOT = System.getProperty("user.dir");
	public static void main(String[] args) {
		SpringApplication.run(StudentCaptureApplication.class, args);
	}
}
