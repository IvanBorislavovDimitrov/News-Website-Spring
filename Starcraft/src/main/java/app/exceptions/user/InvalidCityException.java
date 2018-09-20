package app.exceptions.user;

public class InvalidCityException extends UserRegisterException {

    public InvalidCityException() {
    }

    public InvalidCityException(String message) {
        super(message);
    }
}
