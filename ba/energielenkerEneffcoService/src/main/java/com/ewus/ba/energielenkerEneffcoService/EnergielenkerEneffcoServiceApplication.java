package com.ewus.ba.energielenkerEneffcoService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class EnergielenkerEneffcoServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(EnergielenkerEneffcoServiceApplication.class, args);
  }
}
