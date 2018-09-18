package app.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import app.dtos.CommentDto;
import app.models.Article;
import app.models.Comment;
import app.models.User;
import app.repositories.GenericRepository;
import app.services.api.ArticleService;
import app.services.api.CommentService;
import app.services.api.UserService;

@Service
public class CommentServiceImpl implements CommentService {

    private final GenericRepository<Comment> commentRepository;
    private final UserService userService;
    private final ArticleService articleService;

    @Autowired
    public CommentServiceImpl(@Qualifier(value = "Comment") GenericRepository<Comment> commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Override
    public CommentDto getByIdForRemovePage(int id) {
        Comment comment = this.commentRepository.getById(id);
        CommentDto commentDto = new CommentDto();
        String date = comment.getDate().toString().split("\\s+")[0];
        commentDto.setDate(date);
        commentDto.setValue(comment.getValue());
        commentDto.setId(comment.getId());
        commentDto.setArticleId(comment.getArticle().getId());
        commentDto.setUsername(comment.getUser().getUsername());

        return commentDto;
    }

    @Override
    public void save(CommentDto commentDto, int articleId, String username) {
        Comment comment = new Comment();
        User user = this.userService.getByUsername(username);
        Article article = this.articleService.getArticleById(articleId);

        comment.setValue(commentDto.getValue());
        comment.setUser(user);
        comment.setArticle(article);
        String currentLocalDate = LocalDate.now().toString();
        try {
            comment.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(currentLocalDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.getComments().add(comment);
        article.getComments().add(comment);

        this.commentRepository.save(comment);
    }

    @Override
    public void deleteComment(int commentId, int articleId, String username) {
        Article article = this.articleService.getArticleById(articleId);
        article.getComments().removeIf(c -> c.getId() == commentId);
        User user = this.userService.getByUsername(username);
        user.getComments().removeIf(c -> c.getId() == commentId);
        Comment comment = this.commentRepository.getById(commentId);
        this.commentRepository.delete(comment);
    }

    @Override
    public void update(CommentDto commentDto) {
        Comment comment = this.commentRepository.getById(commentDto.getId());
        comment.setValue(commentDto.getValue());

        this.commentRepository.update(comment);
    }
}
