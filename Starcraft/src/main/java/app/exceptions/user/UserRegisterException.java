package app.exceptions.user;

public abstract class UserRegisterException extends RuntimeException {

    public UserRegisterException() {
    }

    public UserRegisterException(String message) {
        super(message);
    }
}
