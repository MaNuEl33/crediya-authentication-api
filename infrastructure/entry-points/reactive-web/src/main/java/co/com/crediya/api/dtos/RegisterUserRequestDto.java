package co.com.crediya.api.dtos;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterUserRequestDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        LocalDate birthDate,
        String address,
        @NotBlank @Email String email,
        String phoneNumber,
        String documentNumber,
        @NotNull @DecimalMin("0") @DecimalMax("15000000") BigDecimal baseSalary,
        @NotNull Long roleId) {
}
