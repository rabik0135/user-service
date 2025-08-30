package com.rabinchuk.userservice.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
public record UserResponseDto(
        Long id,
        String name,
        String surname,
        LocalDate birthDate,
        String email,
        List<CardInfoResponseDto> cards
) implements Serializable {
}
