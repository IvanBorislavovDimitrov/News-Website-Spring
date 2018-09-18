package app.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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

    private final GenericRepository<Comment> commentService;
    private final UserService userService;
    private final ArticleService articleService;


    @Autowired
    public CommentServiceImpl(@Qualifier(value = "Comment") GenericRepository<Comment> commentService, UserService userService, ArticleService articleService) {
        this.commentService = commentService;
        this.userService = userService;
        this.articleService = articleService;
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

        this.commentService.save(comment);
    }

}
