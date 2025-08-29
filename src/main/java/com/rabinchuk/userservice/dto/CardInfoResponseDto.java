package com.rabinchuk.userservice.dto;

import java.io.Serializable;
import java.time.LocalDate;

public record CardInfoResponseDto(
        Long id,
        Long userId,
        String number,
        String holder,
        LocalDate expirationDate
) implements Serializable {
}
