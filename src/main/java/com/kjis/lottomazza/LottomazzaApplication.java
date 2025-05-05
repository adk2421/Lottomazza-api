package com.kjis.lottomazza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LottomazzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LottomazzaApplication.class, args);
	}

}
