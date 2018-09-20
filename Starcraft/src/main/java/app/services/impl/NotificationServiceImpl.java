package app.services.impl;

import app.dtos.user_dtos.RegisterUserDto;
import app.services.api.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final String DEFAULT_MAIL_SENDER = "automaticMailSenderCommunity@gmail.com";

    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendNotification(RegisterUserDto user)  {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setFrom(DEFAULT_MAIL_SENDER);

        String subject = String.format("Greetings, %s.", user.getUsername());
        simpleMailMessage.setSubject(subject);

        String text = String.format("Thank you for registering  in our website Community. We will ensure you with the best user experience you have ever imagined!%s Kind regards!", System.lineSeparator());
        simpleMailMessage.setText(text);

        this.javaMailSender.send(simpleMailMessage);

    }
}
