package co.com.crediya.api.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDto(
        @NotBlank(message = "The email is required or must not be empty.")
        String email,
        @NotBlank(message = "The password is required or must not be empty.")
        String password
) {
}
