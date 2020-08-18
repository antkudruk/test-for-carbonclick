package com.carbonclick.tsttask.secretsanta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SecretsantaApplication {
	public static void main(String[] args) {
		SpringApplication.run(SecretsantaApplication.class, args);
	}
}
