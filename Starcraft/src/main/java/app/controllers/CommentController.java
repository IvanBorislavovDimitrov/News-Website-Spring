package app.controllers;

import app.dtos.ArticleDto;
import app.dtos.CommentDto;
import app.services.api.ArticleService;
import app.services.api.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }


    @GetMapping(value = "/addComment/{articleId}/{username}")
    @PreAuthorize("isAuthenticated()")
    public String loadAddCommentPage(@PathVariable String articleId, @PathVariable String username, Model model) {
        ArticleDto articleDto = this.articleService.getDtoById(Integer.parseInt(articleId));
        model.addAttribute("article", articleDto);
        model.addAttribute("user", username);

        return "/main/news/addComment";
    }

    @PostMapping(value = "/addComment/{articleId}/{username}")
    @PreAuthorize("isAuthenticated()")
    public String addComment(@PathVariable(name = "articleId") String articleId,
                             @PathVariable(name = "username") String username, CommentDto comment) {
        comment.setUsername(username);
        this.commentService.save(comment, Integer.parseInt(articleId), username);

        return "redirect:/" + "article/" + articleId;
    }

    @GetMapping(value = "/deleteComment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String loadDeleteCommentPage(@PathVariable(name = "commentId") String commentId, Model model) {
        CommentDto commentDto = this.commentService.getByIdForRemovePage(Integer.parseInt(commentId));
        String currentLoggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isNotAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));

        if (!currentLoggedUsername.equals(commentDto.getUsername()) && isNotAdmin) {
            return "main/notAllowed/forbidden";
        }

        model.addAttribute("comment", commentDto);

        return "main/news/deleteComment";
    }

    @PostMapping(value = "/deleteComment/{commentId}/{articleId}/{username}")
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable(name = "commentId") String commentId,
                                @PathVariable(name = "articleId") String articleId,
                                @PathVariable(name = "username") String username) {
        this.commentService.deleteComment(Integer.parseInt(commentId), Integer.parseInt(articleId), username);

        return "redirect:/article/" + articleId;
    }

    @GetMapping(value = "/editComment/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public String loadEditCommentPage(@PathVariable(name = "commentId") String commentId, Model model) {
        CommentDto commentDto = this.commentService.getByIdForRemovePage(Integer.parseInt(commentId));
        String currentLoggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isNotAdmin = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .noneMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
        if (!currentLoggedUsername.equals(commentDto.getUsername()) && isNotAdmin) {
            return "main/notAllowed/forbidden";
        }

        model.addAttribute("comment", commentDto);

        return "main/news/editComment";
    }

    @PostMapping(value = "/editComment/{commentId}/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public String editComment(@PathVariable(name = "commentId") String commentId,
                              @PathVariable(name = "articleId") String articleId,
                              CommentDto commentDto) {
        commentDto.setId(Integer.parseInt(commentId));

        this.commentService.update(commentDto);

        return "redirect:/article/" + articleId;
    }
}
