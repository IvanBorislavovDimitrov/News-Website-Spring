package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import app.dtos.user_dtos.RegisterUserDto;
import app.dtos.user_dtos.UserProfileDto;
import app.services.api.UserService;

@Controller
public class UserController {

    public UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    @PreAuthorize("isAnonymous()")
    public String registerUser(RegisterUserDto registerUserDto) {
        try {
            this.userService.register(registerUserDto);
        } catch (IllegalArgumentException e) {

            return "redirect:/registerError";
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
    public String editProfile(@PathVariable("username") String username, RegisterUserDto registerUserDto) {
        this.userService.updateProfile(username, registerUserDto);

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

    @GetMapping(value = "/registerError")
    @PreAuthorize("isAuthenticated()")
    public String loadRegisterErrorPage(Model model) {
        model.addAttribute("registerError", true);

        return "main/user/register";
    }
}