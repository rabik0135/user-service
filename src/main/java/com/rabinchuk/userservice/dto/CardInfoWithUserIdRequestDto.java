package com.rabinchuk.userservice.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CardInfoWithUserIdRequestDto(

        @NotNull(message = "User id is required")
        Long userId,

        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
        String number,

        @NotBlank(message = "Card holder is required")
        @Size(max = 100, message = "Card holder must not exceed 100 characters")
        String holder,

        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration date must be in the future")
        LocalDate expirationDate
) {
}
