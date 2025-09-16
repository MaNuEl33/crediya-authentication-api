package co.com.crediya.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SearchUsersResponseDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String address,
        String email,
        String phoneNumber,
        String documentNumber,
        BigDecimal baseSalary,
        Long roleId) {
}
