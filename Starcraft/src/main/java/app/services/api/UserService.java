package app.services.api;

import app.dtos.privilege_dtos.ChangePrivilegesDto;
import app.dtos.user_dtos.RegisterUserDto;
import app.dtos.user_dtos.UserProfileDto;
import app.models.User;

import java.util.List;

public interface UserService {
	
	void register(RegisterUserDto registerUserDto);
	
	User getByUsername(String username);
	
	UserProfileDto getUserProfileDto(String username);

	void updateProfilePicture(String username, String avatarName);

	void updateProfile(String username, RegisterUserDto registerUserDto);

	void delete(String username);

    void changePrivileges(String username, ChangePrivilegesDto privilegesDto);

	List<String> getAllUserEmailsWithNames();

    String removeProfileAvatar(String username);
}
