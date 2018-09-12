package app.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import app.dtos.RegisterUserDto;
import app.services.api.UserService;

@Controller
public class SecurityController {
	
	private final UserService userService;
	
	@Autowired
	public SecurityController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping(value = "/register")
	public String registerUser(RegisterUserDto registerUserDto) {
		this.userService.register(registerUserDto);
		
		return "redirect:/login";
	}
	
	@GetMapping(value = "/register")
	public String loadRegisterPage() {
		
		return "main/register";
	}

	@GetMapping(value = "/login")
	public String loadLoginPage() {
		
		return "main/login";
	}
	
	@GetMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		
		return "redirect:/";
	}
}
