package app.services.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.dtos.RegisterUserDto;
import app.models.Privilege;
import app.models.User;
import app.repositories.GenericRepository;
import app.services.api.PrivilegeService;
import app.services.api.UserService;
import app.validationUtil.ValidationUtil;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final GenericRepository<User> userRepository;
	private final PrivilegeService privilegeService;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(@Qualifier("User") GenericRepository<User> userRepository, PasswordEncoder passwordEncoder,
			PrivilegeService privilegeService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.privilegeService = privilegeService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<User> users = this.userRepository.getAll();

		User user = users.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}

		Set<Privilege> roles = user.getPrivileges();
		Set<SimpleGrantedAuthority> grantedAuthorities = roles.stream()
				.map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())).collect(Collectors.toSet());

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), grantedAuthorities);

		return userDetails;
	}

	@Override
	public void register(RegisterUserDto registerUserDto) {
		if (!ValidationUtil.isValid(registerUserDto)) {
			// TODO IMPLEMENT EXCEPTIONS
			throw new IllegalArgumentException("Invalid form!");
		}

		User user = new User();
		user.setUsername(registerUserDto.getUsername());
		user.setEmail(registerUserDto.getEmail());
		String hashedPassword = this.passwordEncoder.encode(registerUserDto.getPassword());
		user.setPassword(hashedPassword);
		user.setFirstName(registerUserDto.getFirstName());
		user.setLastName(registerUserDto.getLastName());

		this.privilegeService.createDefaultPrivilegesIfNeeded();
		if (this.userRepository.getAll().isEmpty()) {
			Privilege privilege = this.privilegeService.getPrivilegeByName("ADMIN");
			user.getPrivileges().add(privilege);
			privilege.getUsers().add(user);
		} else {
			Privilege privilege = this.privilegeService.getPrivilegeByName("USER");
			user.getPrivileges().add(privilege);
			privilege.getUsers().add(user);
		}

		this.userRepository.save(user);
	}

	@Override
	public User getByUsername(String username) {
		return this.userRepository.getAll().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
	}

}
