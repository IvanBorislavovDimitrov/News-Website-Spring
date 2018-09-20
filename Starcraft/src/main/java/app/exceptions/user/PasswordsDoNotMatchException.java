package app.exceptions.user;

public class PasswordsDoNotMatchException extends UserRegisterException {

    public PasswordsDoNotMatchException() {
    }

    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
