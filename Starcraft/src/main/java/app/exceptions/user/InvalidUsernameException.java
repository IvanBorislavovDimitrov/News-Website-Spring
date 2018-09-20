package app.exceptions.user;

public class InvalidUsernameException extends UserRegisterException {

    public InvalidUsernameException() {
    }

    public InvalidUsernameException(String message) {
        super(message);
    }
}
