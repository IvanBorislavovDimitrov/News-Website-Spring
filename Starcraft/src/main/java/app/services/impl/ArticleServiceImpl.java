package app.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import app.dtos.ArticleDto;
import app.dtos.CommentDto;
import app.dtos.RegisterArticleDto;
import app.models.Article;
import app.repositories.GenericRepository;
import app.services.api.ArticleService;
import app.validationUtil.ValidationUtil;

@Service
public class ArticleServiceImpl implements ArticleService {

	private final GenericRepository<Article> articleRepository;

	@Autowired
	public ArticleServiceImpl(@Qualifier("Article") GenericRepository<Article> articleRepository) {
		this.articleRepository = articleRepository;
	}

	@Override
	public List<ArticleDto> getAll() {
		return this.articleRepository.getAll().stream().map(n -> {
			int desiredLength = n.getDescription().length() / 4 > 10 ? 10 : n.getDescription().length() / 4; 
			String shortDescription = n.getDescription().substring(0, 0) + "...";
			String dateString = n.getDate().toString();
			dateString = dateString.substring(0, dateString.indexOf(" "));
			ArticleDto newDto = new ArticleDto(n.getId(), n.getName(), shortDescription, dateString);
			n.getComments().forEach(x -> {
				CommentDto comment = new CommentDto(x.getUser().getUsername(), x.getValue());
				newDto.getComments().add(comment);
			});

			return newDto;
		}).collect(Collectors.toList());
	}

	@Override
	public void save(RegisterArticleDto articleDto) {
		if (!ValidationUtil.isValid(articleDto)) {
			throw new IllegalArgumentException("Invalid article!");
		}
		
		Article article = new Article();
		article.setName(articleDto.getName());
		article.setDescription(articleDto.getDescription());
		Date date = null;
		try {
			String asd = articleDto.getDate();
			date = new SimpleDateFormat("yyyy-MM-dd").parse(articleDto.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		article.setDate(date);
		
		this.articleRepository.save(article);
	}

	@Override
	public ArticleDto getbyId(int id) {
		Article article = this.articleRepository.getById(id);
		ArticleDto articleDto = new ArticleDto();
		articleDto.setId(article.getId());
		articleDto.setName(article.getName());
		articleDto.setDescription(article.getDescription());
		String currentDate = article.getDate().toString();
		currentDate = currentDate.substring(0, currentDate.indexOf(" "));
		articleDto.setDate(currentDate);
		article.getComments().forEach(c -> {
			CommentDto commentDto = new CommentDto();
			commentDto.setUsername(c.getUser().getUsername());
			commentDto.setValue(c.getValue());
			String date = LocalDate.now().toString();
			commentDto.setDate(date);
			articleDto.getComments().add(commentDto);
		});
		
		return articleDto;
	}

	@Override
	public void editArticle(int id, RegisterArticleDto editedArticleInfo) {
		Article article = this.articleRepository.getById(id);
		article.setName(editedArticleInfo.getName());
		article.setDescription(editedArticleInfo.getDescription());
		
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(editedArticleInfo.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		article.setDate(date);
		
		this.articleRepository.update(article);
	}

	@Override
	public void deleteArticle(int id) {
		this.articleRepository.deleteById(id);
	}

	@Override
	public Article getArticleById(int articleId) {
		return this.articleRepository.getById(articleId);
	}

}
