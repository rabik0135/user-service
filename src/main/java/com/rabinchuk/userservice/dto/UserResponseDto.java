package com.rabinchuk.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
@Schema(description = "Data Transfer Object for user information response")
public record UserResponseDto(
        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,
        @Schema(description = "User's first name", example = "John")
        String name,
        @Schema(description = "User's last name", example = "Doe")
        String surname,
        @Schema(description = "User's date of birth", example = "1990-01-15")
        LocalDate birthDate,
        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,
        @Schema(description = "List of cards associated with the user")
        List<CardInfoResponseDto> cards
) implements Serializable {
}
