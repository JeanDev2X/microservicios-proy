package com.mitocode.eureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ApiEurekaServerV1Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiEurekaServerV1Application.class, args);
	}

}
