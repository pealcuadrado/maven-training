package com.everis.training.client;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ContactsConfiguration {
	
	@Bean
	public URI target() {
		return URI.create("http://localhost:1980");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public Client client() {
		return new Client(restTemplate(),target());
	}

}
