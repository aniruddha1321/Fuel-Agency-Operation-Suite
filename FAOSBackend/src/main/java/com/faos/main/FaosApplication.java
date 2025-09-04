package com.faos.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.faos.*")
@EntityScan("com.faos.model")
@EnableJpaRepositories("com.faos.repository")
@EnableScheduling
public class FaosApplication {
	public static void main(String[] args) {
		SpringApplication.run(FaosApplication.class, args);
	}
}
