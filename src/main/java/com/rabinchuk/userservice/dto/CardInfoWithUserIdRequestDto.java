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
@Schema(description = "Data Transfer Object for creating a new card or updating an existing one, specifying the user ID")
public record CardInfoWithUserIdRequestDto(
        @Schema(description = "The ID of the user who owns the card", example = "1")
        @NotNull(message = "User id is required")
        Long userId,

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
