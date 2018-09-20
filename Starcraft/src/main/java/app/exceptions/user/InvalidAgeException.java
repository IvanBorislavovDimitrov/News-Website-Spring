package app.exceptions.user;

public class InvalidAgeException extends UserRegisterException {

    public InvalidAgeException() {
    }

    public InvalidAgeException(String message) {
        super(message);
    }
}
