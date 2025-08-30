package com.rabinchuk.userservice.service;

import com.rabinchuk.userservice.dto.*;
import com.rabinchuk.userservice.model.CardInfo;
import com.rabinchuk.userservice.model.User;

import java.time.LocalDate;
import java.util.Collections;

public class TestDataFactory {

    public static User createUser1() {
        return User.builder()
                .id(1L)
                .name("Michael")
                .surname("Jordan")
                .birthDate(LocalDate.of(1963, 2, 17))
                .email("michael.jordan@gmail.com")
                .build();
    }

    public static UserResponseDto createUserResponseDto1() {
        return UserResponseDto.builder()
                .id(1L)
                .name("Michael")
                .surname("Jordan")
                .birthDate(LocalDate.of(1963, 2, 17))
                .email("michael.jordan@gmail.com")
                .cards(Collections.emptyList())
                .build();
    }

    public static UserRequestDto createUserRequestDto1() {
        return UserRequestDto.builder()
                .name("Michael")
                .surname("Jordan")
                .birthDate(LocalDate.of(1963, 2, 17))
                .email("michael.jordan@gmail.com")
                .build();
    }

    public static User createUser2() {
        return User.builder()
                .id(2L)
                .name("Michael")
                .surname("Jackson")
                .birthDate(LocalDate.of(1958, 8, 29))
                .email("miccael.jackson@gmail.com")
                .build();
    }

    public static UserResponseDto createUserResponseDto2() {
        return UserResponseDto.builder()
                .id(2L)
                .name("Michael")
                .surname("Jackson")
                .birthDate(LocalDate.of(1958, 8, 29))
                .email("miccael.jackson@gmail.com")
                .cards(Collections.emptyList())
                .build();
    }

    public static CardInfo createCardInfo1(User user) {
        return CardInfo.builder()
                .id(1L)
                .user(user)
                .number("1234567890123456")
                .holder("holder")
                .expirationDate(LocalDate.of(2030, 1, 1))
                .build();
    }

    public static CardInfoResponseDto createCardInfoResponseDto1() {
        return CardInfoResponseDto.builder()
                .id(1L)
                .userId(1L)
                .number("1234567890123456")
                .holder("holder")
                .expirationDate(LocalDate.of(2030, 1, 1))
                .build();
    }

    public static CardInfoRequestDto createCardInfoRequestDto1() {
        return CardInfoRequestDto.builder()
                .number("1234567890123456")
                .holder("holder")
                .expirationDate(LocalDate.of(2030, 1, 1))
                .build();
    }

    public static CardInfoWithUserIdRequestDto createCardInfoWithUserIdRequestDto1() {
        return CardInfoWithUserIdRequestDto.builder()
                .userId(1L)
                .number("1234567890123456")
                .holder("holder")
                .expirationDate(LocalDate.of(2030, 1, 1))
                .build();
    }

    public static CardInfo createCardInfo2(User user) {
        return CardInfo.builder()
                .id(2L)
                .user(user)
                .number("1122334455667788")
                .holder("holder")
                .expirationDate(LocalDate.of(2030, 1, 1))
                .build();
    }

    public static CardInfoResponseDto createCardInfoResponseDto2() {
        return CardInfoResponseDto.builder()
                .id(2L)
                .userId(1L)
                .number("1122334455667788")
                .holder("holder")
                .expirationDate(LocalDate.of(2030, 1, 1))
                .build();
    }

}
