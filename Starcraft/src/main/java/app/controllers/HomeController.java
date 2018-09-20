package app.controllers;

import app.dtos.article_dtos.ArticleDto;
import app.pages.Page;
import app.services.api.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

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
        int pagesCount = (int) Math.ceil(size / 2D);
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
        List<ArticleDto> articleDtos = this.articleService.getAll().stream().limit(3).collect(Collectors.toList());
        articleDtos.sort((d1, d2) -> d2.getDate().compareTo(d1.getDate()));

        if (articleDtos.size() > 0) {
            model.addAttribute("article1", articleDtos.get(0));
        }
        if (articleDtos.size() > 1) {
            model.addAttribute("article2", articleDtos.get(1));
        }
        if (articleDtos.size() > 2) {
            model.addAttribute("article3", articleDtos.get(2));
        }

        return "main/index";
    }

    private void addArticlesToPages(List<ArticleDto> news, Page[] pages, int count) {
        for (int i = 0; count < news.size(); i++) {
            pages[i] = new Page();
            pages[i].setNumber(i + 1);
            pages[i].getArticles().add(news.get(count++));
            if (count == news.size())
                break;
            pages[i].getArticles().add(news.get(count++));
        }
    }
}
