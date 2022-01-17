package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@SpringBootApplication
public class HospitaFrontendApplication {
	
	final static String BACKEND_API_BASE_URI = "http://localhost:8080/";
	
	@Bean
	public RestTemplate getRestTemplate() {
		DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(BACKEND_API_BASE_URI);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(uriBuilderFactory);
		return restTemplate;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HospitaFrontendApplication.class, args);
	}

}
