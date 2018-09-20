package app.services.impl;

import app.dtos.article_dtos.RegisterArticleDto;
import app.dtos.user_dtos.RegisterUserDto;
import app.services.api.NotificationService;
import app.services.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String DEFAULT_MAIL_SENDER = "automaticMailSenderCommunity@gmail.com";

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    @Autowired
    public NotificationServiceImpl(JavaMailSender javaMailSender, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    @Override
    public void sendNotificationForRegistering(RegisterUserDto user) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setFrom(DEFAULT_MAIL_SENDER);

        String subject = String.format("Greetings, %s.", user.getUsername());
        simpleMailMessage.setSubject(subject);

        String text = String.format("Thank you for registering  in our website Community. We will ensure you with the best user experience you have ever imagined!%s Kind regards!", System.lineSeparator());
        simpleMailMessage.setText(text);

        this.javaMailSender.send(simpleMailMessage);
    }

    @Override
    public void sendNotificationForNewArticle(RegisterArticleDto article) {
        List<String> emails = this.userService.getAllUserEmailsWithNames();
        for (String emailAndUser : emails) {
            String[] tokens = emailAndUser.split("&");
            String email = tokens[0];
            String username = tokens[1];

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setFrom(DEFAULT_MAIL_SENDER);

            String subject = String.format("Greetings, %s.", username);
            simpleMailMessage.setSubject(subject);

            String text = "It's a pleasure for us to tell you that we've just announced a new article \'" + article.getName() +
                    "\'. Come and visit our website for the full information about this article. Have a great day. Kind regards, Community.";
            simpleMailMessage.setText(text);

            this.javaMailSender.send(simpleMailMessage);
        }
    }
}
