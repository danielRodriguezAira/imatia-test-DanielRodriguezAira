package com.imatia.statemachine.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.imatia.statemachine"})
public class StateMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(StateMachineApplication.class, args);
	}

}
