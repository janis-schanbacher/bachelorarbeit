package com.ewus.ba.energielenkerService;

import com.ewus.ba.energielenkerService.controller.EnergielenkerController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EnergielenkerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergielenkerServiceApplication.class, args);
	}

}
