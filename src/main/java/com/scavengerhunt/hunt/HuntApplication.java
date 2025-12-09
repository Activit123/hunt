package com.scavengerhunt.hunt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HuntApplication {

	public static void main(String[] args) {
		SpringApplication.run(HuntApplication.class, args);
	}

}
