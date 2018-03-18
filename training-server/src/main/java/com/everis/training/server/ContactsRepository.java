package com.everis.training.server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import src.com.everis.training.api.ContactResource;

@Service
public class ContactsRepository {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ContactsRepository.class);

	private long createdContacts;
	private final Map<Long,ContactResource> contacts;
	private final Map<Long,String> contactNameIndex;
	private final MultiValueMap<String,Long> contactsBySurname;
	
	public ContactsRepository() {
		this.createdContacts=0;
		this.contacts=new LinkedHashMap<>();
		this.contactNameIndex=new LinkedHashMap<>();
		this.contactsBySurname=new LinkedMultiValueMap<>();
	}

	public synchronized void add(ContactResource contact) {
		final Optional<ContactResource> matching=findContact(contact.getName(), contact.getSurname());
		if(matching.isPresent()) {
			LOGGER.info("Persistence failure: Contact {} {} already exists ({})",contact.getName(),contact.getSurname(),matching.get());
			throw 
				new IllegalArgumentException(
					String.format("Contact %s %s already exists (%d)",contact.getName(),contact.getSurname(),matching.get().getId()));
		}
		contact.setId(++createdContacts);
		contacts.put(contact.getId(),contact);
		contactNameIndex.put(contact.getId(),contact.getName());
		contactsBySurname.add(contact.getSurname(),contact.getId());
		LOGGER.info("Persisted contact {}",contact);
	}

	public synchronized ContactResource findById(long id) {
		return contacts.get(id);
	}

	public synchronized ContactResource remove(long id) {
		ContactResource contact = contacts.get(id);
		if(contact!=null) {
			contactNameIndex.remove(id);
			contactsBySurname.remove(contact.getSurname(),id);
			contacts.remove(id);
			LOGGER.info("Removed contact {}",contact);
		}
		return contact;
	}

	private Optional<ContactResource> findContact(String name, String surname) {
		return
			Optional.
				ofNullable(contactsBySurname.get(surname)).
				orElse(new ArrayList<Long>()).
					stream().
						filter(id -> {
							return name.equals(contactNameIndex.get(id));
						}).
						map(id -> contacts.get(id)).
						findFirst();
	}

}