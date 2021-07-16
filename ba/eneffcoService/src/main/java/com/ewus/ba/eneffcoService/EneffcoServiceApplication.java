package com.ewus.ba.eneffcoService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EneffcoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EneffcoServiceApplication.class, args);
	}

}
