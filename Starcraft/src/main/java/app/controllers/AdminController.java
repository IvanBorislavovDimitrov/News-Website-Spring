package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import app.dtos.ArticleDto;
import app.dtos.RegisterArticleDto;
import app.services.api.ArticleService;

@Controller
public class AdminController {

    private final ArticleService articleService;

    @Autowired
    public AdminController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/adminPanel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadAdminPanel() {

        return "main/admin/adminPanel";
    }

}
