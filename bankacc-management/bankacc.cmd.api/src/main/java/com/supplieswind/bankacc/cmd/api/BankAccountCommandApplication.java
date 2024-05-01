package com.supplieswind.bankacc.cmd.api;

import com.supplieswind.bankacc.core.configuration.CustomAxonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({
		CustomAxonConfiguration.class
})
public class BankAccountCommandApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountCommandApplication.class, args);
	}

}
