package com.everis.training.server;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import src.com.everis.training.api.ContactResource;
import src.com.everis.training.api.CreateContactRequest;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ContactsController.class);

	private final ContactsService service;

	public ContactsController(@Autowired ContactsService service) {
		this.service = service;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes="application/json", produces="application/json")
	public ResponseEntity<ContactResource> createContact(@RequestBody CreateContactRequest contact) {
		LOGGER.info("Creating contact from: {}",contact);
		final ContactResource resource = this.service.createContact(contact);
		final URI location=
			ServletUriComponentsBuilder.
				fromCurrentRequest().
					path("/{id}").
					buildAndExpand(resource.getId()).
					toUri();
		return
			ResponseEntity.
				created(location).
						body(resource);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value="{id}", produces="application/json")
	public ResponseEntity<ContactResource> getContact(@PathVariable long id) {
		LOGGER.info("Retrieving contact {}...",id);
		final Optional<ContactResource> contact = this.service.getContact(id);
		if(!contact.isPresent()) {
			LOGGER.info("Unknown contact {}",id);
			throw new IllegalStateException("Unknown contact");
		}
		LOGGER.info("Found contact {}",contact.get());
		return ResponseEntity.ok(contact.get());
	}

	@ResponseStatus(HttpStatus.OK)
	@DeleteMapping(value="{id}", produces="application/json")
	public void removeContact(@PathVariable long id) {
		LOGGER.info("Removing contact {}...",id);
		final Optional<ContactResource> contact = this.service.removeContact(id);
		if(!contact.isPresent()) {
			LOGGER.info("Unknown contact {}",id);
			throw new IllegalStateException("Unknown contact");
		}
		LOGGER.info("Removed contact {}",contact.get());
	}

}
