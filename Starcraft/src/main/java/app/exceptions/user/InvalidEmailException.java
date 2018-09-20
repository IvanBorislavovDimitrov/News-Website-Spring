package app.exceptions.user;

public class InvalidEmailException extends UserRegisterException {

    public InvalidEmailException() {
    }

    public InvalidEmailException(String message) {
        super(message);
    }
}
