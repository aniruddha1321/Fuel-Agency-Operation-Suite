package com.faos.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.faos.*")
@EntityScan("com.faos.model")
@EnableJpaRepositories("com.faos.repository")
public class Faos1Application {
	public static void main(String[] args) {
		SpringApplication.run(Faos1Application.class, args);
	}
}