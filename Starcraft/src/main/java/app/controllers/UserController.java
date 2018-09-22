package app.controllers;

import app.dtos.user_dtos.RegisterUserDto;
import app.dtos.user_dtos.UserProfileDto;
import app.exceptions.user.UserRegisterException;
import app.services.api.FileUploadService;
import app.services.api.NotificationService;
import app.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final FileUploadService fileUploadService;

    @Autowired
    public UserController(UserService userService, NotificationService notificationService, FileUploadService fileUploadService) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/register")
    @PreAuthorize("isAnonymous()")
    public String registerUser(RegisterUserDto registerUserDto, Model model,
                               @RequestParam(name = "avatar", required = false) MultipartFile avatar) {
        try {
            if (avatar != null) {
                String avatarName = avatar.getOriginalFilename();
                registerUserDto.setAvatarName(avatarName);
            }
            // register user
            this.userService.register(registerUserDto);
            // add the avatar to avatars' dir
            if (avatar != null) {
                this.fileUploadService.saveAvatar(avatar, registerUserDto.getUsername());
            }
            // send a notification
            this.notificationService.sendNotificationForRegistering(registerUserDto);
        } catch (UserRegisterException e) {
            String exceptionName = e.getClass().getSimpleName();
            exceptionName = Character.toLowerCase(exceptionName.charAt(0)) + exceptionName.substring(1);
            model.addAttribute(exceptionName, true);

            return "main/user/register";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/login";
    }

    @GetMapping(value = "/register")
    @PreAuthorize("isAnonymous()")
    public String loadRegisterPage() {

        return "main/user/register";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String loadProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserName = authentication.getName();
        UserProfileDto userProfileDto = this.userService.getUserProfileDto(loggedUserName);

        model.addAttribute("user", userProfileDto);

        return "main/user/profile";
    }

    @GetMapping(value = "/editProfile/{username}")
    @PreAuthorize("isAuthenticated()")
    public String loadEditProfilePage(@PathVariable("username") String username, Model model) {
        String currentLoggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!currentLoggedUsername.equals(username)) {
            return "main/notAllowed/forbidden";
        }

        UserProfileDto userProfileDto = this.userService.getUserProfileDto(username);
        model.addAttribute("user", userProfileDto);

        return "main/user/editProfile";
    }

    @PostMapping(value = "/editProfile/{username}")
    @PreAuthorize("isAuthenticated()")
    public String editProfile(@PathVariable("username") String username, RegisterUserDto registerUserDto, Model model) {
        try {
            this.userService.updateProfile(username, registerUserDto);
        } catch (UserRegisterException e) {
            String exceptionName = e.getClass().getSimpleName();
            exceptionName = Character.toLowerCase(exceptionName.charAt(0)) + exceptionName.substring(1);
            model.addAttribute(exceptionName, true);
            UserProfileDto userProfileDto = this.userService.getUserProfileDto(username);
            model.addAttribute("user", userProfileDto);

            return "main/user/editProfile";
        }

        return "redirect:/profile";
    }

    @GetMapping(value = "/deleteProfile/{username}")
    @PreAuthorize("isAuthenticated()")
    public String loadDeleteProfilePage(@PathVariable("username") String username, Model model) {
        String currentLoggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isNotAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));

        if (!currentLoggedUsername.equals(username) && isNotAdmin) {
            return "main/notAllowed/forbidden";
        }

        UserProfileDto userProfileDto = this.userService.getUserProfileDto(username);
        model.addAttribute("user", userProfileDto);

        return "main/user/deleteProfile";
    }

    @PostMapping(value = "/deleteProfile/{username}")
    @PreAuthorize("isAuthenticated()")
    public String deleteProfile(@PathVariable("username") String username) {
        this.userService.delete(username);
        String currentlyLoggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentlyLoggedUsername.equals(username)) {
            return "redirect:/logout";
        }

        return "redirect:/";
    }
}