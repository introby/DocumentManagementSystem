package by.intro.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DocumentManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumentManagementSystemApplication.class, args);
	}

}
