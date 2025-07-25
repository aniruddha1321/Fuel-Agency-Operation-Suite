package com.faos.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.faos")
public class CmClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(CmClientApplication.class, args);
	}
}
