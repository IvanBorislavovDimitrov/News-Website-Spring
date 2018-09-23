package app.controllers;

import app.dtos.article_dtos.ArticleDto;
import app.pages.Page;
import app.services.api.ArticleService;
import app.utilities.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private static final int MAX_ARTICLE_ON_PAGE = 6;

    private final ArticleService articleService;

    @Autowired
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/news")
    public String home(@RequestParam(name = "page", defaultValue = "1") String page, @RequestParam(name = "articleName", required = false) String articleName, Model model) {

        List<ArticleDto> news = this.articleService.getAll();
        news.sort((n1, n2) -> n2.getDate().compareTo(n1.getDate()));

        int size = news.size();
        int pagesCount = (int) Math.ceil(size / (double) MAX_ARTICLE_ON_PAGE);
        Page[] pages = new Page[pagesCount];
        int count = 0;
        this.addArticlesToPages(news, pages, count);

        int requiredPage = Integer.parseInt(page) - 1;

        model.addAttribute("pages", pages);
        model.addAttribute("page", pages[requiredPage]);
        model.addAttribute("pageNumber", requiredPage + 1);
        model.addAttribute("maxPages", pagesCount + 1);
        model.addAttribute("minPages", 0);

        return "main/news";
    }

    @GetMapping(value = "/ownerInformation")
    public String loadOwnerInfoPage() {

        return "main/admin/ownerInformation";
    }

    @GetMapping(value = "/")
    public String loadIndexPage(Model model) {
        List<ArticleDto> articleDtos = this.articleService.getAll().stream()
                .limit(3)
                .sorted((d1, d2) -> d2.getDate().compareTo(d1.getDate()))
                .collect(Collectors.toList());

        for (int i = 0; i < articleDtos.size() && i < 3; i++) {
            String articleVal = "article" + (i + 1);
            model.addAttribute(articleVal, articleDtos.get(i));
        }

        return "main/index";
    }

    private void addArticlesToPages(List<ArticleDto> news, Page[] pages, int count) {
        Helper.addArticlesToPages(news, pages, count, MAX_ARTICLE_ON_PAGE);
    }
}
