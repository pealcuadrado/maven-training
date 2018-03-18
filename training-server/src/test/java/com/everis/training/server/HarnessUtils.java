package com.everis.training.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import src.com.everis.training.api.ContactResource;
import src.com.everis.training.api.CreateContactRequest;
import src.com.everis.training.model.Position;

class HarnessUtils {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected final CreateContactRequest addNamedContact(String name, String surname) {
		final Position position = new Position();
		position.setTitle("Developer");
		position.setCompany("ACME");
		final CreateContactRequest object=new CreateContactRequest();
		object.setName(name);
		object.setSurname(surname);
		object.setPosition(position);
		return object;
	}

	protected final ContactResource namedContactResource(String name, String surname) {
		final Position position = new Position();
		position.setTitle("Developer");
		position.setCompany("ACME");
		final ContactResource object=new ContactResource();
		object.setName(name);
		object.setSurname(surname);
		object.setPosition(position);
		return object;
	}
}
