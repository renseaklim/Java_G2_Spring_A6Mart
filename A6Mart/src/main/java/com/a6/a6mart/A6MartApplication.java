package com.a6.a6mart;

import com.a6.a6mart.api.models.AppUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.a6.a6mart.repositories")
public class A6MartApplication {

	public static void main(String[] args) {
		SpringApplication.run(A6MartApplication.class, args);
	}

}
