package app.services.api;

import app.dtos.CommentDto;

public interface CommentService {

    CommentDto getByIdForRemovePage(int id);

    void save(CommentDto comment, int articleId, String username);

    void deleteComment(int commentId, int articleId, String username);

    void update(CommentDto commentDto);
}
