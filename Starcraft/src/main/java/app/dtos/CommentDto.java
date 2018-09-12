package app.dtos;

public class CommentDto {
	
	private String username;
	private String value;
	private String date;
	
	public CommentDto() {
		
	}
	
	public CommentDto(String username, String value) {
		this.username = username;
		this.value = value;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
