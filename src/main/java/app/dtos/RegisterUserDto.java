package app.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterUserDto {

	@Size(min = 3, max = 50)
	private String username;
	
	@Pattern(regexp = "[A-Za-z0-9]+@[A-Za-z]{2,}(\\.[a-zA-Z]{2,})+")
	private String email;
	
	@Size(min = 3, max = 50)
	private String password;

	@Size(min = 3, max = 50)
	private String firstName;

	@Size(min = 3, max = 50)
	private String lastName;
	
	public RegisterUserDto() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
