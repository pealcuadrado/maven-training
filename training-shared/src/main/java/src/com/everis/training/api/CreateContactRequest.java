package src.com.everis.training.api;

import src.com.everis.training.model.Contact;

public class CreateContactRequest extends Contact {

	@Override
	public String toString() {
		return String.format(
				"CreateContactRequest [name=%s, surname=%s, position=%s]", getName(), getSurname(), getPosition());
	}

}
