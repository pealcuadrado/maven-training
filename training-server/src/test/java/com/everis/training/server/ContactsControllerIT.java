package com.everis.training.server;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.everis.training.server.ContactsApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import src.com.everis.training.api.ContactResource;
import src.com.everis.training.api.CreateContactRequest;
import src.com.everis.training.model.Position;

@SpringBootTest(classes=ContactsApplication.class)
public class ContactsControllerIT extends AbstractControllerTest {

	@Configuration
	static class Config {
		
		@Bean
		public ObjectMapper objectMapper() {
			return new ObjectMapper();
		}
		
	}
	
	private JacksonTester<CreateContactRequest> request;
	private JacksonTester<ContactResource> response;

	@Before
	public void setup() throws IOException {
		JacksonTester.initFields(this,super.objectMapper);
	}

	@Test
	public void shouldCreateNewUsers() throws Exception {
		final CreateContactRequest object = defaultContact();
		long before = System.currentTimeMillis();
		this.mockMvc.
			perform(
				MockMvcRequestBuilders.
					post("/contacts").
						content(this.request.write(object).getJson()).
						contentType(MediaType.APPLICATION_JSON_UTF8).
						accept(MediaType.APPLICATION_JSON)).
			andDo(MockMvcResultHandlers.print()).
			andExpect(status().isCreated()).
			andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8)).
			andExpect(header().string(HttpHeaders.LOCATION, startsWith("http://localhost/contacts/"))).
			andExpect(jsonPath("$.id",instanceOf(Number.class))).
			andExpect(jsonPath("$.creationTimestamp",greaterThan(before))).
			andExpect(jsonPath("$.name", equalTo(object.getName()))).
			andExpect(jsonPath("$.surname", equalTo(object.getSurname()))).
			andExpect(jsonPath("$.position.title", equalTo(object.getPosition().getTitle()))).
			andExpect(jsonPath("$.position.company", equalTo(object.getPosition().getCompany())));
	}

	@Test
	public void shouldNotCreateConflictingContacts() throws Exception {
		final CreateContactRequest object = addNamedContact("John","Doe");
		final ContactResource resource = createContact(object);
		this.mockMvc.
			perform(
				MockMvcRequestBuilders.
					post("/contacts").
						content(this.request.write(object).getJson()).
						contentType(MediaType.APPLICATION_JSON_UTF8).
						accept(MediaType.APPLICATION_JSON)).
			andExpect(status().isConflict()).
			andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8)).
			andExpect(jsonPath("$.status",equalTo(HttpStatus.CONFLICT.value()))).
			andExpect(jsonPath("$.error",equalTo(HttpStatus.CONFLICT.getReasonPhrase()))).
			andExpect(jsonPath("$.description", equalTo("Contact "+resource.getName()+" "+resource.getSurname()+" already exists ("+resource.getId()+")")));
	}

	@Test
	public void shouldReturnExistingContacts() throws Exception {
		final CreateContactRequest object = addNamedContact("Jane","Doe");
		final ContactResource resource = createContact(object);
		this.mockMvc.
			perform(
				MockMvcRequestBuilders.
					get("/contacts/"+resource.getId()).
						accept(MediaType.APPLICATION_JSON)).
			andExpect(status().isOk()).
			andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8)).
			andExpect(jsonPath("$.id",equalTo((int)resource.getId()))).
			andExpect(jsonPath("$.creationTimestamp",equalTo(resource.getCreationTimestamp()))).
			andExpect(jsonPath("$.name", equalTo(object.getName()))).
			andExpect(jsonPath("$.surname", equalTo(object.getSurname()))).
			andExpect(jsonPath("$.position.title", equalTo(object.getPosition().getTitle()))).
			andExpect(jsonPath("$.position.company", equalTo(object.getPosition().getCompany())));
	}

	private ContactResource createContact(final CreateContactRequest object) throws Exception, IOException, UnsupportedEncodingException {
		final MvcResult result = 
			this.mockMvc.
				perform(
					MockMvcRequestBuilders.
						post("/contacts").
							content(this.request.write(object).getJson()).
							contentType(MediaType.APPLICATION_JSON_UTF8).
							accept(MediaType.APPLICATION_JSON)).
				andExpect(status().isCreated()).
				andReturn();
		return this.response.parseObject(result.getResponse().getContentAsString());
	}

	private CreateContactRequest defaultContact() {
		final Position position = new Position();
		position.setTitle("Solutions Specific Knowledge Analyst");
		position.setCompany("everis");
		final CreateContactRequest object=new CreateContactRequest();
		object.setName("Miguel");
		object.setSurname("Esteban Gutiérrez");
		object.setPosition(position);
		return object;
	}

}
