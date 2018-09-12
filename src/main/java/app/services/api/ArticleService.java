package app.services.api;

import java.util.List;

import app.dtos.*;
import app.models.Article;

public interface ArticleService {

	List<ArticleDto> getAll();

	void save(RegisterArticleDto article);
	
	ArticleDto getbyId(int id);
	
	void editArticle(int id, RegisterArticleDto editedArticleInfo);
	
	void deleteArticle(int id);

	Article getArticleById(int articleId);
}
