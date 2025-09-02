package com.rabinchuk.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
@Schema(description = "Data Transfer Object for card information response")
public record CardInfoResponseDto(
        @Schema(description = "Unique identifier of the card", example = "1")
        Long id,
        @Schema(description = "ID of the user who owns the card", example = "1")
        Long userId,
        @Schema(description = "Masked card number", example = "************4242")
        String number,
        @Schema(description = "Name of the cardholder", example = "John Doe")
        String holder,
        @Schema(description = "Card's expiration date", example = "2028-12-31")
        LocalDate expirationDate
) implements Serializable {
}
