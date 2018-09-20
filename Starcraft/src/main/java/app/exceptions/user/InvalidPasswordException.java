package app.exceptions.user;

public class InvalidPasswordException extends UserRegisterException {

    public InvalidPasswordException() {
    }

    public InvalidPasswordException(String message) {
        super(message);
    }
}
