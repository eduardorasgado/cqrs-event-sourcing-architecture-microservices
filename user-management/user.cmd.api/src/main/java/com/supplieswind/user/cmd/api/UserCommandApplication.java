package com.supplieswind.user.cmd.api;

import com.supplieswind.user.core.configuration.CustomAxonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		CustomAxonConfiguration.class
})
public class UserCommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCommandApplication.class, args);
	}

}
