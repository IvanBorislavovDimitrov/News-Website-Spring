package app.services.api;

import app.dtos.CommentDto;

public interface CommentService {
	
	void save(CommentDto comment, int articleId, String username);
}
