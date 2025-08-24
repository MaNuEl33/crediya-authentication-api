package co.com.crediya.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder
public class RegisterUserRequestDto {

    @NotBlank
    @JsonProperty("first_name")
    String firstName;

    @NotBlank
    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("birth_date")
    LocalDate birthDate;

    String address;

    @NotBlank
    @Email
    String email;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("document_number")
    String documentNumber;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("15000000")
    @JsonProperty("base_salary")
    BigDecimal baseSalary;

    @NotNull
    @JsonProperty("role_id")
    Long roleId;
}
