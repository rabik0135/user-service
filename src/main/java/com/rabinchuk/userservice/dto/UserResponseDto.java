package com.rabinchuk.userservice.dto;

import java.time.LocalDate;
import java.util.List;

public record UserResponseDto(
        Long id,
        String name,
        String surname,
        LocalDate birthDate,
        String email,
        List<CardInfoResponseDto> cards
) {
}
