package app.controllers;

import app.pages.Page;
import app.services.api.NotificationService;
import app.services.api.UserService;
import app.utilities.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import app.dtos.article_dtos.ArticleDto;
import app.dtos.article_dtos.RegisterArticleDto;
import app.services.api.ArticleService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ArticlesController {

    private static final int MAX_ARTICLE_ON_PAGE = 6;

    private final NotificationService notificationService;
    private final ArticleService articleService;
    private final UserService userService;

    @Autowired
    public ArticlesController(NotificationService notificationService, ArticleService articleService, UserService userService) {
        this.notificationService = notificationService;
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping(value = "/addArticle")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadAddArticlePage() {

        return "main/news/addArticle";
    }

    @PostMapping(value = "/addArticle")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addArticle(RegisterArticleDto article) {
        this.articleService.save(article);
        this.notificationService.sendNotificationForNewArticle(article);
        return "redirect:/";
    }

    @GetMapping(value = "/article/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public String loadArticleDetailsPage(@PathVariable String articleId, Model model) {
        ArticleDto articleDto = this.articleService.getDtoById(Integer.parseInt(articleId));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentlyLoggedUsername = auth.getName();

        model.addAttribute("username", currentlyLoggedUsername);
        model.addAttribute("article", articleDto);
        articleDto.getComments().sort((c1, c2) -> c2.getDate().compareTo(c1.getDate()));
        model.addAttribute("comments", articleDto.getComments());

        return "/main/news/viewArticle";
    }

    @GetMapping(value = "/editArticle/{articleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadEditArticlePage(@PathVariable String articleId, Model model) {
        ArticleDto articleDto = this.articleService.getDtoById(Integer.parseInt(articleId));
        model.addAttribute("article", articleDto);

        return "/main/news/editArticle";
    }

    @PostMapping(value = "/editArticle/{articleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editArticle(@PathVariable String articleId, RegisterArticleDto editedArticleInfo) {
        this.articleService.editArticle(Integer.parseInt(articleId), editedArticleInfo);

        return "redirect:/";
    }

    @GetMapping(value = "/deleteArticle/{articleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String loadDeleteArticlePage(@PathVariable String articleId, Model model) {
        ArticleDto articleDto = this.articleService.getDtoById(Integer.parseInt(articleId));
        model.addAttribute("article", articleDto);

        return "/main/news/deleteArticle";
    }

    @PostMapping(value = "/deleteArticle/{articleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteArticle(@PathVariable String articleId, RegisterArticleDto editedArticleInfo) {
        this.articleService.deleteArticle(Integer.parseInt(articleId));

        return "redirect:/";
    }

    @GetMapping(value = "/search")
    public String loadSearchPageAndResults(@RequestParam("articleName") String articleName,
            @RequestParam(name = "page", defaultValue = "1") String page, Model model) {
        List<ArticleDto> news = this.articleService.getAll().stream()
                .filter(a -> a.getName().toLowerCase().contains(articleName.toLowerCase()))
                .sorted((a1, a2) -> a2.getDate().compareTo(a1.getDate()))
                .collect(Collectors.toList());

        int size = news.size();
        int pagesCount = (int) Math.ceil(size / (double) MAX_ARTICLE_ON_PAGE);
        Page[] pages = new Page[pagesCount];
        int count = 0;
        this.addArticlesToPages(news, pages, count);

        int requiredPage = Integer.parseInt(page) - 1;

        if (pagesCount == 0 || news.size() == 0) {
            model.addAttribute("areThereNews", false);

            return "/main/news/search";
        }

        model.addAttribute("areThereNews", true);
        model.addAttribute("news", pages[requiredPage].getArticles());
        model.addAttribute("pages", pages);
        model.addAttribute("pageNumber", requiredPage + 1);
        model.addAttribute("articleName", articleName);
        model.addAttribute("maxPages", pagesCount + 1);
        model.addAttribute("minPages", 0);

        return "/main/news/search";
    }

    private void addArticlesToPages(List<ArticleDto> news, Page[] pages, int count) {
        Helper.addArticlesToPages(news, pages, count, MAX_ARTICLE_ON_PAGE);
    }
}
