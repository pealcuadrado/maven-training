package com.everis.training.client;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import src.com.everis.training.api.ContactResource;

@Component
public class Client {

	private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

	private final RestTemplate restTemplate;

	private URI target;

	public Client(@Autowired RestTemplate restTemplate, @Autowired URI target) {
		this.restTemplate = restTemplate;
		this.target = target;
	}
	
	public ContactResource create(String filename) {
		LOGGER.info("Requesting creation of contact defined in file {}...", filename);
		try {
			String body =
				Files.
					readAllLines(Paths.get(filename)).
						stream().
						collect(Collectors.joining(System.lineSeparator()));
			final HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
			headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			final RequestEntity<String> request = new RequestEntity<String>(body, headers, HttpMethod.POST,target.resolve("/contacts"));
			final ResponseEntity<ContactResource> response = this.restTemplate.exchange(request, ContactResource.class);
			LOGGER.info("Created contact: {}", response.getBody());
			LOGGER.info("Available at...: {}", response.getHeaders().get(HttpHeaders.LOCATION));
			return response.getBody();
		} catch (Exception e) {
			LOGGER.error("Could not create contact for payload defined in {}. Full stack trace follows", filename, e);
			return null;
		}
	}

	public ContactResource retrieve(String id) {
		LOGGER.info("Requesting retrieval of contact {}...", id);
		try {
			final HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
			final RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.GET,target.resolve("/contacts/"+id));
			final ResponseEntity<ContactResource> response = this.restTemplate.exchange(request,ContactResource.class);
			ContactResource result=response.getBody();
			LOGGER.info("Retrieved contact: {}", result);
			return result;
		} catch(HttpClientErrorException e) {
			LOGGER.warn("Could not find contact {}: {} ({})",id,HttpStatus.valueOf(e.getRawStatusCode()).getReasonPhrase(),e.getRawStatusCode());
			return null;
		} catch (Exception e) {
			LOGGER.error("Could not find contact {}. Full stack trace follows",id,e);
			return null;
		}
	}

	public void remove(String id) {
		LOGGER.info("Requesting removal of contact {}...", id);
		try {
			final HttpHeaders headers = new HttpHeaders();
			final RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.DELETE,target.resolve("/contacts/"+id));
			this.restTemplate.exchange(request, Void.class);
			LOGGER.info("Contact removed.");
		} catch(HttpClientErrorException e) {
			LOGGER.warn("Could not remove contact {}: {} ({})",id,HttpStatus.valueOf(e.getRawStatusCode()).getReasonPhrase(),e.getRawStatusCode());
		} catch (Exception e) {
			LOGGER.error("Could not remove contact {}. Full stack trace follows",id,e);
		}
	}

}
