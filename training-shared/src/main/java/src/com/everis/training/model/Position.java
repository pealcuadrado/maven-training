package src.com.everis.training.model;

public class Position {
	
	private String company;
	private String title;

	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return String.format("Position [company=%s, title=%s]", company, title);
	}

}