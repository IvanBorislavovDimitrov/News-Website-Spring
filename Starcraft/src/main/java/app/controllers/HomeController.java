package app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.dtos.ArticleDto;
import app.services.api.ArticleService;

@Controller
public class HomeController {

	private final ArticleService articleService;

	@Autowired
	public HomeController(ArticleService articleService) {
		this.articleService = articleService;
	}

	@RequestMapping(value = "/")
	public String home(Model model) {
		List<ArticleDto> news = this.articleService.getAll();
		news.sort((d1, d2) -> d2.getDate().compareTo(d1.getDate()));
		model.addAttribute("news", news);

		return "main/news";
	}

}
