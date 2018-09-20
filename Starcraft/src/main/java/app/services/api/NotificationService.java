package app.services.api;

import app.dtos.article_dtos.RegisterArticleDto;
import app.dtos.user_dtos.RegisterUserDto;

import java.util.List;

public interface NotificationService {
    void sendNotificationForRegistering(RegisterUserDto user);

    void sendNotificationForNewArticle(RegisterArticleDto article);
}
