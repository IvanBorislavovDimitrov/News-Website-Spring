package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import app.dtos.RegisterArticleDto;
import app.dtos.RegisterUserDto;
import app.dtos.UserProfileDto;
import app.services.api.UserService;

@Controller
public class UserController {

	public UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/profile")
	public String loadProfilePage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String loggedUserName = authentication.getName();
		UserProfileDto userProfileDto = this.userService.getUserProfileDto(loggedUserName);

		model.addAttribute("user", userProfileDto);

		return "main/user/profile";
	}

	@GetMapping(value = "/editProfile/{username}")
	public String loadEditProfilePage(@PathVariable("username") String username, Model model) {
		UserProfileDto userProfileDto = this.userService.getUserProfileDto(username);
		model.addAttribute("user", userProfileDto);

		return "main/user/editProfile";
	}

	@PostMapping(value = "/editProfile/{username}")
	public String editProfile(@PathVariable("username") String username, RegisterUserDto registerUserDto) {
		this.userService.updateProfile(username, registerUserDto);

		return "redirect:/profile";
	}

	@GetMapping(value = "/deleteProfile/{username}")
	public String loadDeleteProfilePage(@PathVariable("username") String username, Model model) {
		UserProfileDto userProfileDto = this.userService.getUserProfileDto(username);
		model.addAttribute("user", userProfileDto);

		return "main/user/deleteProfile";
	}
	
	@PostMapping(value = "/deleteProfile/{username}")
	public String deleteProfile(@PathVariable("username") String username) {
		this.userService.delete(username);
		
		return "redirect:/logout";
	}
}