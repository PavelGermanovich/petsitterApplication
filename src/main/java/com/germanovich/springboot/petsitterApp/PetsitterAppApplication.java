package com.germanovich.springboot.petsitterApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
public class PetsitterAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(PetsitterAppApplication.class, args);
	}
}
