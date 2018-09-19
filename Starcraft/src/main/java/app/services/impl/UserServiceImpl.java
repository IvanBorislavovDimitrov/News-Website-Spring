package app.services.impl;

import java.io.PipedReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import app.dtos.privilege_dtos.ChangePrivilegesDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.dtos.comment_dtos.CommentDto;
import app.dtos.user_dtos.RegisterUserDto;
import app.dtos.user_dtos.UserProfileDto;
import app.models.Privilege;
import app.models.User;
import app.repositories.GenericRepository;
import app.services.api.PrivilegeService;
import app.services.api.UserService;
import app.validationUtil.ValidationUtil;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String ROLE_USER = "USER";
    private static final String ROLE_ADMIN = "ADMIN";

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
        user.setAge(registerUserDto.getAge());
        user.setCity(registerUserDto.getCity());

        this.privilegeService.createDefaultPrivilegesIfNeeded();
        if (this.userRepository.getAll().isEmpty()) {
            this.updatePrivilege(user, ROLE_ADMIN);

        } else {
            this.updatePrivilege(user, ROLE_USER);
        }
    }

    private void updatePrivilege(User user, String role) {
        Privilege privilege = this.privilegeService.getPrivilegeByName(role);
        user.getPrivileges().add(privilege);
        privilege.getUsers().add(user);
        this.userRepository.save(user);

        this.privilegeService.update(privilege);
    }

    @Override
    public User getByUsername(String username) {
        return this.userRepository.getAll().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    @Override
    public UserProfileDto getUserProfileDto(String username) {
        User user = this.getByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format("User %s not found!", username));
        }
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUsername(user.getUsername());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setFirstName(user.getFirstName());
        userProfileDto.setLastName(user.getLastName());
        userProfileDto.setAge(user.getAge());
        userProfileDto.setCity(user.getCity());
        user.getComments().forEach(c -> {
            CommentDto commentDto = new CommentDto();
            commentDto.setUsername(c.getUser().getUsername());
            commentDto.setValue(c.getValue());
            commentDto.setDate(c.getDate().toString());
            userProfileDto.getComments().add(commentDto);
        });

        return userProfileDto;
    }

    @Override
    public void updateProfile(String username, RegisterUserDto registerUserDto) {
        User user = this.getByUsername(username);
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        String encodedPassword = this.passwordEncoder.encode(registerUserDto.getPassword());
        user.setPassword(encodedPassword);
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setAge(registerUserDto.getAge());
        user.setCity(registerUserDto.getCity());

        this.userRepository.update(user);
    }

    @Override
    public void delete(String username) {
        User user = this.getByUsername(username);
        this.privilegeService.removeUser(username);
        this.userRepository.delete(user);
    }

    @Override
    public void changePrivileges(String username, ChangePrivilegesDto privilegesDto) {
        User user = this.userRepository.getAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found!");
        }

        Privilege privilegeAdmin = this.privilegeService.getPrivilegeByName(ROLE_ADMIN);
        Privilege privilegeUser = this.privilegeService.getPrivilegeByName(ROLE_USER);

        if (privilegesDto.isAdmin()) {
            if (user.getPrivileges().stream().noneMatch(p -> p.getName().equals(privilegeAdmin.getName()))) {
                user.getPrivileges().add(privilegeAdmin);
                privilegeAdmin.getUsers().add(user);
            }
        } else {
            user.getPrivileges().removeIf(p -> p.getName().equals(privilegeAdmin.getName()));
            privilegeAdmin.getUsers().removeIf(u -> u.getUsername().equals(username));
        }

        this.userRepository.update(user);
        this.privilegeService.update(privilegeAdmin);

        if (privilegesDto.isUser()) {
            if (user.getPrivileges().stream().noneMatch(p -> p.getName().equals(privilegeUser.getName()))) {
                user.getPrivileges().add(privilegeUser);
                privilegeUser.getUsers().add(user);
            }
        } else {
            user.getPrivileges().removeIf(p -> p.getName().equals(privilegeUser.getName()));
            privilegeUser.getUsers().removeIf(u -> u.getUsername().equals(username));
        }

        this.userRepository.update(user);
        this.privilegeService.update(privilegeUser);

    }
}
