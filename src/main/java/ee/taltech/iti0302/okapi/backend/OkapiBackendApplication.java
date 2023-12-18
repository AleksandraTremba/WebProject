package ee.taltech.iti0302.okapi.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OkapiBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkapiBackendApplication.class, args);
	}
}
