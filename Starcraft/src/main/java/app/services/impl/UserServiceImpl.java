package app.services.impl;

import app.dtos.comment_dtos.CommentDto;
import app.dtos.privilege_dtos.ChangePrivilegesDto;
import app.dtos.user_dtos.RegisterUserDto;
import app.dtos.user_dtos.UserProfileDto;
import app.exceptions.user.*;
import app.models.Privilege;
import app.models.User;
import app.repositories.GenericRepository;
import app.services.api.PrivilegeService;
import app.services.api.UserService;
import app.validationUtil.ValidationUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public void register(RegisterUserDto registerUserDto) throws UserRegisterException {
        this.makeValidationsForUser(registerUserDto);

        User user = new User();
        this.setUserDetails(registerUserDto, user);


        this.privilegeService.createDefaultPrivilegesIfNeeded();
        this.setRoleToUser(user);
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
        userProfileDto.setAvatarName(user.getAvatarName());
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
    public void updateProfile(String username, RegisterUserDto editedDto) throws UserRegisterException {
        User user = this.getByUsername(username);
        this.makeValidationsForExistingUser(editedDto);
        this.setUserDetails(editedDto, user);

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

    @Override
    public List<String> getAllUserEmailsWithNames() {
        List<String> allEmailsWithUsersnames = this.userRepository.getAll().stream().map(u -> u.getEmail() + "&" + u.getUsername()).collect(Collectors.toList());

        return allEmailsWithUsersnames;
    }

    private void checkIfUsernameIsTaken(String username) {
        if (this.userRepository.getAll().stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new UsernameTakenException("This username is already taken!");
        }
    }

    private void checkIfEmailIsTaken(String email) {
        if (this.userRepository.getAll().stream().anyMatch(u -> u.getEmail().equals(email))) {
            throw new EmailTakenException("This email is already taken!");
        }
    }

    private void checkIfPasswordsMatch(String psw1, String psw2) {
        if (!psw1.equals(psw2)) {
            throw new PasswordsDoNotMatchException("Passwords don't match!");
        }
    }

    private void checkAge(Integer age) {
        if (age == null || age < 0 || age > 150) {
            throw new InvalidAgeException("Invalid age!");
        }
    }

    private void checkCity(String city) {
        if (city.length() < 2 || city.length() > 50) {
            throw new InvalidCityException("City name too short!");
        }
    }

    private void checkEmail(String email) {
        String regex = "[A-Za-z0-9]+@[A-Za-z]{2,}(\\.[a-zA-Z]{2,})+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidEmailException("Invalid email!");
        }
    }

    private void checkFirstName(String firstName) {
        if (firstName.length() < 2 || firstName.length() > 50) {
            throw new InvalidFirstNameException("First name is not valid!");
        }
    }

    private void checkLastName(String lastName) {
        if (lastName.length() < 2 || lastName.length() > 50) {
            throw new InvalidLastNameException("Last name is not valid!");
        }
    }

    private void checkPassword(String password) {
        if (password.length() < 2 || password.length() > 50) {
            throw new InvalidPasswordException("Password is not valid!");
        }
    }

    private void checkUsername(String username) {
        if (username.length() < 2 || username.length() > 50) {
            throw new InvalidUsernameException("Username is not valid!");
        }
        String regex = "[a-zA-Z]\\w+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            throw new InvalidUsernameException("Username is not valid!");
        }
    }

    private void makeValidationsForUser(RegisterUserDto registerUserDto) {
        this.checkIfEmailIsTaken(registerUserDto.getEmail());
        this.checkIfUsernameIsTaken(registerUserDto.getUsername());
        this.checkIfPasswordsMatch(registerUserDto.getPassword(), registerUserDto.getConfirmPassword());
        this.makeAdditionalCheck(registerUserDto);
    }

    private void setUserDetails(RegisterUserDto registerUserDto, User user) {
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        String hashedPassword = this.passwordEncoder.encode(registerUserDto.getPassword());
        user.setPassword(hashedPassword);
        user.setFirstName(registerUserDto.getFirstName());
        user.setLastName(registerUserDto.getLastName());
        user.setAge(registerUserDto.getAge());
        user.setCity(registerUserDto.getCity());
        user.setAvatarName(registerUserDto.getAvatarName());
    }

    private void updatePrivilege(User user, String role) {
        Privilege privilege = this.privilegeService.getPrivilegeByName(role);
        user.getPrivileges().add(privilege);
        privilege.getUsers().add(user);
        this.userRepository.save(user);

        this.privilegeService.update(privilege);
    }

    private void setRoleToUser(User user) {
        if (this.userRepository.getAll().isEmpty()) {
            this.updatePrivilege(user, ROLE_ADMIN);
        } else {
            this.updatePrivilege(user, ROLE_USER);
        }
    }

    private void makeValidationsForExistingUser(RegisterUserDto editedDto) {
        this.checkIfPasswordsMatch(editedDto.getPassword(), editedDto.getConfirmPassword());
        this.makeAdditionalCheck(editedDto);
    }

    private void makeAdditionalCheck(RegisterUserDto editedDto) {
        if (!ValidationUtil.isValid(editedDto)) {
            this.checkUsername(editedDto.getUsername());
            this.checkEmail(editedDto.getEmail());
            this.checkPassword(editedDto.getPassword());
            this.checkFirstName(editedDto.getFirstName());
            this.checkLastName(editedDto.getLastName());
            this.checkAge(editedDto.getAge());
            this.checkCity(editedDto.getCity());
        }
    }
}
