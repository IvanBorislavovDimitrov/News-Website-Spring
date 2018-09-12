package app.services.api;

import app.dtos.RegisterUserDto;
import app.models.User;

public interface UserService {
	
	void register(RegisterUserDto registerUserDto);
	
	User getByUsername(String username);
}
