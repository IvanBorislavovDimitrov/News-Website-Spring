package app.exceptions.user;

public class EmailTakenException extends UserRegisterException {
    public EmailTakenException() {
    }

    public EmailTakenException(String message) {
        super(message);
    }
}
