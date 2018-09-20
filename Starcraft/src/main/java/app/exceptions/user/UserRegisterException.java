package app.exceptions.user;

public class UserRegisterException extends RuntimeException {

    public UserRegisterException() {
    }

    public UserRegisterException(String message) {
        super(message);
    }
}
