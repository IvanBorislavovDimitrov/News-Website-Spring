package app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import app.dtos.ArticleDto;
import app.dtos.CommentDto;
import app.dtos.RegisterArticleDto;
import app.models.Comment;
import app.services.api.ArticleService;
import app.services.api.CommentService;

@Controller
public class ArticlesController {

	private final ArticleService articleService;
	private final CommentService commentService;

	@Autowired
	public ArticlesController(ArticleService articleService, CommentService commentService) {
		this.articleService = articleService;
		this.commentService = commentService;
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
		
		return "redirect:/";
	}
	
	@GetMapping(value = "/article/{articleId}")
	public String loadArticleDetailsPage(@PathVariable String articleId, Model model) {
		ArticleDto articleDto = this.articleService.getbyId(Integer.parseInt(articleId));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentlyLoggedUsername = auth.getName();
		
		model.addAttribute("username", currentlyLoggedUsername);
		model.addAttribute("article", articleDto);
		articleDto.getComments().sort((c1, c2) -> c2.getDate().compareTo(c1.getDate()));
		model.addAttribute("comments", articleDto.getComments());
		
		return "/main/news/viewArticle";
	}
	
	@GetMapping(value ="/editArticle/{articleId}")
	public String loadEditArticlePage(@PathVariable String articleId, Model model) {
		ArticleDto articleDto = this.articleService.getbyId(Integer.parseInt(articleId));
		model.addAttribute("article", articleDto);
		
		return "/main/news/editArticle";
	}
	
	@PostMapping(value = "/editArticle/{articleId}")
	public String editArticle(@PathVariable String articleId, RegisterArticleDto editedArticleInfo) {
		this.articleService.editArticle(Integer.parseInt(articleId), editedArticleInfo);
		
		return "redirect:/";
	}
	
	@GetMapping(value = "/deleteArticle/{articleId}")
	public String loadDeleteArticlePage(@PathVariable String articleId, Model model) {
		ArticleDto articleDto = this.articleService.getbyId(Integer.parseInt(articleId));
		model.addAttribute("article", articleDto);
		
		return "/main/news/deleteArticle";
	}
	
	@PostMapping(value = "/deleteArticle/{articleId}")
	public String deleteArticle(@PathVariable String articleId, RegisterArticleDto editedArticleInfo) {
		this.articleService.deleteArticle(Integer.parseInt(articleId));
		
		return "redirect:/";
	}
	
	@GetMapping(value = "/addComment/{articleId}/{username}")
	public String loadAddCommentPage(@PathVariable String articleId, @PathVariable String username, Model model) {
		ArticleDto articleDto = this.articleService.getbyId(Integer.parseInt(articleId));
		model.addAttribute("article", articleDto);
		model.addAttribute("user", username);
		
		return "/main/news/addComment";
	}
	
	@PostMapping(value = "/addComment/{articleId}/{username}")
	public String addComment(@PathVariable(name = "articleId") String articleId,
			@PathVariable(name = "username") String username, CommentDto comment) {
		comment.setUsername(username);
		this.commentService.save(comment, Integer.parseInt(articleId), username);
		
		return "redirect:/" + "article/" + articleId;
	}
}
