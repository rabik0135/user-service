package com.rabinchuk.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Data Transfer Object for creating or updating a user")
public record UserRequestDto(
        @Schema(description = "User's first name", example = "John")
        @NotBlank(message = "Name is required")
        @Size(max = 50, message = "Name must not exceed 50 characters")
        String name,

        @Schema(description = "User's last name", example = "Doe")
        @NotBlank(message = "Surname is required")
        @Size(max = 50, message = "Surname must not exceed 50 characters")
        String surname,

        @Schema(description = "User's date of birth", example = "1990-01-15")
        @NotNull(message = "Birth date is required")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @Schema(description = "User's unique email address", example = "john.doe@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email
) {
}
