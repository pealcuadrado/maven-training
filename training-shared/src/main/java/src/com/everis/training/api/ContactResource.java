package src.com.everis.training.api;

import src.com.everis.training.model.Contact;

public class ContactResource extends Contact {
	
	private long id;
	private long creationTimestamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(long creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	@Override
	public String toString() {
		return String.format(
				"ContactResource [id=%s, creationTimestamp=%s, name=%s, surname=%s, position=%s]", id,
				creationTimestamp, getName(), getSurname(), getPosition());
	}

}
