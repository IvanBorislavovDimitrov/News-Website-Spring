package app.exceptions.user;

public class InvalidFirstNameException extends UserRegisterException {

    public InvalidFirstNameException() {
    }

    public InvalidFirstNameException(String message) {
        super(message);
    }
}
