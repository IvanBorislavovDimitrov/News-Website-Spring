package app.controllers;

import app.dtos.privilege_dtos.ChangePrivilegesDto;
import app.dtos.privilege_dtos.PrivilegeDto;
import app.dtos.user_dtos.UserProfileDto;
import app.services.api.PrivilegeService;
import app.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;
    private final PrivilegeService privilegeService;

    @Autowired
    public AdminController(UserService userService, PrivilegeService privilegeService) {
        this.userService = userService;
        this.privilegeService = privilegeService;
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
            try {
                UserProfileDto userProfileDto = this.userService.getUserProfileDto(username);
                model.addAttribute("user", userProfileDto);
            } catch (IllegalArgumentException e) {
                model.addAttribute("userNotFound", true);
                model.addAttribute("notFoundMessage", e.getMessage());
            }
        }

        return "main/admin/changePrivilege";
    }

    @GetMapping(value = "/editProfilePage/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadEditProfilePrivilegePage(@PathVariable(name = "username", required = false) String username, Model model) {
        if (username == null) {
            username = "";
        }
        List<PrivilegeDto> privilegeDtos = this.privilegeService.getPrivilegeDtos();

        model.addAttribute("privileges", privilegeDtos);
        model.addAttribute("username", username);

        return "main/admin/editProfilePrivileges";
    }

    @PostMapping(value = "/editProfilePage/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editProfilePrivileges(@PathVariable(name = "username") String username, ChangePrivilegesDto privilegesDto) {
        this.userService.changePrivileges(username, privilegesDto);

        return "redirect:/";
    }
}
