package app.controllers;

import app.dtos.UserProfileDto;
import app.models.User;
import app.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.dtos.ArticleDto;
import app.dtos.RegisterArticleDto;
import app.services.api.ArticleService;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/adminPanel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadAdminPanel() {

        return "main/admin/adminPanel";
    }

    @GetMapping(value = "/changePrivilege")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadChangePrivilegePage(@RequestParam(name = "username", required = false) String username, Model model) {
        if (username != null) {
            UserProfileDto userProfileDto = this.userService.getUserProfileDto(username);
            model.addAttribute("user", userProfileDto);
        }

        return "main/admin/changePrivilege";
    }
}
