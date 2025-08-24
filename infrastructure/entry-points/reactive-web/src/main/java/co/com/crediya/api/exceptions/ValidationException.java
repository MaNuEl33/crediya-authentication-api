package co.com.crediya.api.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class ValidationException extends RuntimeException {
    private final transient Set<? extends ConstraintViolation<?>> violations;
}
