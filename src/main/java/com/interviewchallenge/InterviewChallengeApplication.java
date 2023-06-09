package com.interviewchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class InterviewChallengeApplication  {

	public static void main(String[] args) {
		SpringApplication.run(InterviewChallengeApplication.class, args);
	}

}
