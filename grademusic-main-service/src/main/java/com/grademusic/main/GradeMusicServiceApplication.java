package com.grademusic.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GradeMusicServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GradeMusicServiceApplication.class, args);
	}

}
