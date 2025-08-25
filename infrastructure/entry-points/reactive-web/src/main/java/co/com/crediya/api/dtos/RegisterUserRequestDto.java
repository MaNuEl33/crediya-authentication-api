package co.com.crediya.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterUserRequestDto(
        @JsonProperty("first_name") @NotBlank String firstName,
        @JsonProperty("last_name") @NotBlank String lastName,
        @JsonProperty("birth_date") LocalDate birthDate, String address,
        @NotBlank @Email String email, @JsonProperty("phone_number") String phoneNumber,
        @JsonProperty("document_number") String documentNumber,
        @JsonProperty("base_salary") @NotNull @DecimalMin("0") @DecimalMax("15000000") BigDecimal baseSalary,
        @JsonProperty("role_id") @NotNull Long roleId) {
}
