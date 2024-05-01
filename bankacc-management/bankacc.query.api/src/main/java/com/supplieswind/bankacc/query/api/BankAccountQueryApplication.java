package com.supplieswind.bankacc.query.api;

import com.supplieswind.bankacc.core.configuration.CustomAxonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		CustomAxonConfiguration.class
})
public class BankAccountQueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountQueryApplication.class, args);
	}

}
