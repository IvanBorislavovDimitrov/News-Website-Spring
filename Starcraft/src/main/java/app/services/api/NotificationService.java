package app.services.api;

import app.dtos.user_dtos.RegisterUserDto;

public interface NotificationService {
    void sendNotification(RegisterUserDto user);
}
