package com.rabinchuk.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Data Transfer Object for adding a new card to an existing user")
public record CardInfoRequestDto(
        @Schema(description = "The 16-digit card number", example = "4916989612345678")
        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
        String number,

        @Schema(description = "The name of the cardholder as it appears on the card", example = "John Doe")
        @NotBlank(message = "Card holder is required")
        @Size(max = 100, message = "Card holder must not exceed 100 characters")
        String holder,

        @Schema(description = "The card's expiration date (must be in the future)", example = "2028-12-31")
        @NotNull(message = "Expiration date is required")
        @Future(message = "Expiration date must be in the future")
        LocalDate expirationDate
) {
}
