package app.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	public String home(@RequestParam(name = "articleName", required =  false) String articleName, Model model) {
		List<ArticleDto> news = null;
		if (articleName == null) {
			news = this.articleService.getAll();
		} else {
			news = this.articleService.getAll().stream()
					.filter(a -> a.getName().toLowerCase().contains(articleName.toLowerCase()))
					.collect(Collectors.toList());
		}
		news.sort((d1, d2) -> d2.getDate().compareTo(d1.getDate()));
		model.addAttribute("news", news);

		return "main/news";
	}

}
