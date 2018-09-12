package app.services.api;

import app.models.Privilege;

public interface PrivilegeService {

	void createDefaultPrivilegesIfNeeded();
	
	Privilege getPrivilegeByName(String name);
}
