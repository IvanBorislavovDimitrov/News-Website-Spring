package app.services.api;

import app.models.Privilege;
import app.models.User;

public interface PrivilegeService {

	void createDefaultPrivilegesIfNeeded();
	
	Privilege getPrivilegeByName(String name);

	void update(Privilege privilege);

	void removeUser(String username);
}
