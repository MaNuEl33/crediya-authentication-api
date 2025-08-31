package co.com.crediya.usecase.helpers;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class FieldValidatorHelper {

    public static boolean isNotBlank(String field) {
        return field != null && !field.trim().isEmpty();
    }

    public static boolean isInRange(BigDecimal value, BigDecimal min, BigDecimal max) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    public static boolean isValidEmail(String email) {
        final var emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        return isNotBlank(email) && email.matches(emailRegex);
    }

    public static boolean isBlank(String field) {
        return field == null || field.trim().isEmpty();
    }
}
