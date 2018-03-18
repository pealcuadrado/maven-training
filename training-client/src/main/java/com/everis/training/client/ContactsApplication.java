package com.everis.training.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ContactsApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContactsApplication.class);

	private final ApplicationContext ctx;

	@Autowired
	private Client client;

	public ContactsApplication() {
		this.ctx = new AnnotationConfigApplicationContext(ContactsConfiguration.class);
		this.ctx.getAutowireCapableBeanFactory().autowireBean(this);
	}

	public void runApp(String... args) {
		if(args.length!=2) {
			LOGGER.error("Expected only 2 parameters, not {}",args.length);
		}
		if("create".equalsIgnoreCase(args[0])) {
			client.create(args[1]);
		} else if("retrieve".equalsIgnoreCase(args[0])) {
			client.retrieve(args[1]);
		} else {
			LOGGER.error("Unknown command '{}'",args[0]);
		}
	}

	public static void main(String[] args) {
		new ContactsApplication().
			runApp(args);
	}

}
