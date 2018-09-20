package app.exceptions.user;

public class InvalidLastNameException extends UserRegisterException {

    public InvalidLastNameException() {
    }

    public InvalidLastNameException(String message) {
        super(message);
    }
}
