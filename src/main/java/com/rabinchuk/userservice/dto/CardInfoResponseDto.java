package com.rabinchuk.userservice.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

@Builder
public record CardInfoResponseDto(
        Long id,
        Long userId,
        String number,
        String holder,
        LocalDate expirationDate
) implements Serializable {
}
