package app.services.api;

import java.util.List;

import app.dtos.article_dtos.ArticleDto;
import app.dtos.article_dtos.RegisterArticleDto;
import app.models.Article;

public interface ArticleService {

	List<ArticleDto> getAll();

	void save(RegisterArticleDto article);
	
	ArticleDto getDtoById(int id);
	
	void editArticle(int id, RegisterArticleDto editedArticleInfo);
	
	void deleteArticle(int id);

	Article getArticleById(int articleId);
}
