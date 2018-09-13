package app.services.api;

import app.dtos.RegisterUserDto;
import app.dtos.UserProfileDto;
import app.models.User;

public interface UserService {
	
	void register(RegisterUserDto registerUserDto);
	
	User getByUsername(String username);
	
	UserProfileDto getUserProfileDto(String username);

	void updateProfile(String username, RegisterUserDto registerUserDto);

	void delete(String username);
}
