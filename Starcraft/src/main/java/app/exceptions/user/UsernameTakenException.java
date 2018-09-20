package app.exceptions.user;

public class UsernameTakenException extends UserRegisterException {

    public UsernameTakenException() {
    }

    public UsernameTakenException(String reason) {
        super(reason);
    }
}
