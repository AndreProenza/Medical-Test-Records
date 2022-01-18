package com.laboratory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import com.laboratory.utils.BackendUri;

@SpringBootApplication
public class LaboratoryFrontendApplication {
	
	@Bean
	public RestTemplate getRestTemplate() {
		DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(BackendUri.BACKEND_API_BASE_URI);
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(uriBuilderFactory);
		return restTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(LaboratoryFrontendApplication.class, args);
	}

}
