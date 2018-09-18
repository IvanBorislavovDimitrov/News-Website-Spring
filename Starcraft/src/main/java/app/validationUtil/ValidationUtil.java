package app.validationUtil;

import javax.validation.Validation;
import javax.validation.Validator;

public final class ValidationUtil {

    private ValidationUtil() {

    }

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> boolean isValid(T obj) {

        return obj != null && VALIDATOR.validate(obj).size() == 0;
    }
}
