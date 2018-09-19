package app.services.api;

import app.dtos.privilege_dtos.PrivilegeDto;
import app.models.Privilege;

import java.util.List;

public interface PrivilegeService {

	void createDefaultPrivilegesIfNeeded();
	
	Privilege getPrivilegeByName(String name);

	void update(Privilege privilege);

	void removeUser(String username);

    List<PrivilegeDto> getPrivilegeDtos();
}
