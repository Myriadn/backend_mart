package com.muhammad_anas.backend_mart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.muhammad_anas.backend_mart.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties (AppProperties.class)
public class BackendMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendMartApplication.class, args);
	}

}
