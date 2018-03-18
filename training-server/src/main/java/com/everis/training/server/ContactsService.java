package com.everis.training.server;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import src.com.everis.training.api.ContactResource;
import src.com.everis.training.model.Contact;

@Service
public class ContactsService {

	private final ContactsRepository repository;

	public ContactsService(@Autowired ContactsRepository repository) {
		this.repository = repository;
	}
	
	public ContactResource createContact(Contact contact) {
		final ContactResource resource = new ContactResource();
		resource.setName(contact.getName());
		resource.setSurname(contact.getSurname());
		resource.setPosition(contact.getPosition());
		resource.setCreationTimestamp(System.currentTimeMillis());
		this.repository.add(resource);
		return resource;
	}

	public Optional<ContactResource> getContact(long id) {
		return Optional.ofNullable(repository.findById(id));
	}

	public Optional<ContactResource> removeContact(long id) {
		return Optional.ofNullable(repository.remove(id));
	}

}