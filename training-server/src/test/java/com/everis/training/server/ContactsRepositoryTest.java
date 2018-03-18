package com.everis.training.server;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import com.everis.training.server.ContactsRepository;

import src.com.everis.training.api.ContactResource;

public class ContactsRepositoryTest extends HarnessUtils {

	private ContactsRepository sut;

	@Before
	public void setUp() {
		sut = new ContactsRepository();
	}
	
	@Test
	public void shouldInitializeIdentifier() throws Exception {
		ContactResource contact=super.namedContactResource("John","Doe");
		sut.add(contact);
		assertThat(contact.getId(),not(equalTo(0)));
	}

	@Test
	public void shouldRetrievePersistedContacts() throws Exception {
		ContactResource contact=super.namedContactResource("John","Doe");
		sut.add(contact);
		assertThat(sut.findById(contact.getId()),sameInstance(contact));
	}

}
