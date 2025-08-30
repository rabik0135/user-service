package com.rabinchuk.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CardInfoRequestDto(

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
