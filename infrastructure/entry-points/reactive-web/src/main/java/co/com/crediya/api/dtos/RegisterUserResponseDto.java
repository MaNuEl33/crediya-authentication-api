package co.com.crediya.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterUserResponseDto(
        Long id,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("birth_date") LocalDate birthDate, String address, String email,
        @JsonProperty("phone_number") String phoneNumber,
        @JsonProperty("document_number") String documentNumber,
        @JsonProperty("base_salary") BigDecimal baseSalary,
        @JsonProperty("role_id") Long roleId) {
}
