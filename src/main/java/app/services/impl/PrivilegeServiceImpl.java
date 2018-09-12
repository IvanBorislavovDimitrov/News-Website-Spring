package app.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import app.models.Privilege;
import app.repositories.GenericRepository;
import app.services.api.PrivilegeService;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {
	
	private final GenericRepository<Privilege> privilegeRepository;
	
	public PrivilegeServiceImpl(@Qualifier("Privilege") GenericRepository<Privilege> privilegeRepository) {
		this.privilegeRepository = privilegeRepository;
	}

	@Override
	public void createDefaultPrivilegesIfNeeded() {
		List<Privilege> privileges = this.privilegeRepository.getAll();
		if (privileges.isEmpty()) {
			this.createAdminAndUserPrivileges();
		}
	}
	
	private void createAdminAndUserPrivileges() {
		Privilege adminPrivilege = new Privilege();
		adminPrivilege.setName("ADMIN");
		Privilege userPrivilege = new Privilege();
		userPrivilege.setName("USER");
		
		this.privilegeRepository.save(adminPrivilege);
		this.privilegeRepository.save(userPrivilege);
	}

	@Override
	public Privilege getPrivilegeByName(String name) {
		return this.privilegeRepository.getAll().stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}
}
