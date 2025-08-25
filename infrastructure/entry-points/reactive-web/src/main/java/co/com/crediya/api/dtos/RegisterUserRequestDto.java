package co.com.crediya.api.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterUserRequestDto(
        @NotBlank(message = "The first_name field is required.") String firstName,
        @NotBlank(message = "The last_name field is required.") String lastName,
        LocalDate birthDate,
        String address,
        @NotBlank(message = "The email field is required.")
        @Email(message = "The email must be valid.") String email,
        String phoneNumber,
        String documentNumber,
        @NotNull(message = "The base_salary field is required.")
        @DecimalMin(value = "0", message = "The min value for the field base_salary must be 0.")
        @DecimalMax(value = "15000000", message = "The max value for the field base_salary must be 15000000.") BigDecimal baseSalary,
        @NotNull(message = "The role_id field is required.") Long roleId) {
}
