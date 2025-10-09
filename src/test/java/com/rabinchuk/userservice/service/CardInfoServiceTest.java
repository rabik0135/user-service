package com.rabinchuk.userservice.service;

import com.rabinchuk.userservice.dto.CardInfoRequestDto;
import com.rabinchuk.userservice.dto.CardInfoResponseDto;
import com.rabinchuk.userservice.dto.CardInfoWithUserIdRequestDto;
import com.rabinchuk.userservice.mapper.CardInfoMapper;
import com.rabinchuk.userservice.model.CardInfo;
import com.rabinchuk.userservice.model.User;
import com.rabinchuk.userservice.repository.CardInfoRepository;
import com.rabinchuk.userservice.repository.UserRepository;
import com.rabinchuk.userservice.service.impl.CardInfoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardInfoServiceTest {

    @Mock
    private CardInfoRepository cardInfoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardInfoMapper cardInfoMapper;

    @InjectMocks
    private CardInfoServiceImpl cardInfoService;

    CardInfo cardInfo1;
    CardInfoResponseDto cardInfoResponseDto1;
    CardInfoRequestDto cardInfoRequestDto1;
    CardInfoWithUserIdRequestDto cardInfoWithUserIdRequestDto1;

    CardInfo cardInfo2;
    CardInfoResponseDto cardInfoResponseDto2;

    User user;

    @BeforeEach
    void setUp() {
        user = TestDataFactory.createUser1();

        cardInfo1 = TestDataFactory.createCardInfo1(user);
        cardInfoResponseDto1 = TestDataFactory.createCardInfoResponseDto1();
        cardInfoRequestDto1 = TestDataFactory.createCardInfoRequestDto1();
        cardInfoWithUserIdRequestDto1 = TestDataFactory.createCardInfoWithUserIdRequestDto1();

        cardInfo2 = TestDataFactory.createCardInfo2(user);
        cardInfoResponseDto2 = TestDataFactory.createCardInfoResponseDto2();
    }

    @Test
    @DisplayName("Get all cards")
    void testGetAllCards() {
        List<CardInfo> cards = List.of(cardInfo1, cardInfo2);

        when(cardInfoRepository.findAll()).thenReturn(cards);
        when(cardInfoMapper.toDto(cardInfo1)).thenReturn(cardInfoResponseDto1);
        when(cardInfoMapper.toDto(cardInfo2)).thenReturn(cardInfoResponseDto2);
        List<CardInfoResponseDto> expected = List.of(cardInfoResponseDto1, cardInfoResponseDto2);

        List<CardInfoResponseDto> actual = cardInfoService.getAll();

        assertThat(actual).isNotNull().hasSize(2);
        assertEquals(expected, actual);
        verify(cardInfoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get all cards should return empty list")
    void testGetAllCardsShouldReturnEmptyList() {
        when(cardInfoRepository.findAll()).thenReturn(Collections.emptyList());

        List<CardInfoResponseDto> actual = cardInfoService.getAll();

        assertThat(actual).isEmpty();
        verify(cardInfoRepository, times(1)).findAll();
        verify(cardInfoMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Get card by id")
    void testGetCardById() {
        when(cardInfoRepository.findById(1L)).thenReturn(Optional.of(cardInfo1));
        when(cardInfoMapper.toDto(cardInfo1)).thenReturn(cardInfoResponseDto1);

        CardInfoResponseDto actual = cardInfoService.getById(1L);


        assertThat(actual).isNotNull().isEqualTo(cardInfoResponseDto1);
        verify(cardInfoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get card by id should throw exception")
    void testGetCardByIdShouldThrowException() {
        when(cardInfoRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.getById(100L));
    }

    @Test
    @DisplayName("Get cards by ids")
    void testGetCardsByIds() {
        when(cardInfoRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(cardInfo1, cardInfo2));
        when(cardInfoMapper.toDto(cardInfo1)).thenReturn(cardInfoResponseDto1);
        when(cardInfoMapper.toDto(cardInfo2)).thenReturn(cardInfoResponseDto2);
        List<CardInfoResponseDto> expected = List.of(cardInfoResponseDto1, cardInfoResponseDto2);

        List<CardInfoResponseDto> actual = cardInfoService.getByIds(List.of(1L, 2L));

        assertThat(actual).isNotNull().hasSize(2);
        assertEquals(expected, actual);

        verify(cardInfoRepository, times(1)).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Add card to user")
    void testAddCardToUser() {
        assertThat(user.getCards()).isEmpty();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardInfoMapper.toEntity(cardInfoRequestDto1)).thenReturn(cardInfo1);
        when(cardInfoRepository.save(any(CardInfo.class))).thenReturn(cardInfo1);
        when(cardInfoMapper.toDto(cardInfo1)).thenReturn(cardInfoResponseDto1);

        CardInfoResponseDto actual = cardInfoService.addCardInfoToUser(user.getId(), cardInfoRequestDto1);

        assertThat(actual).isNotNull().isEqualTo(cardInfoResponseDto1);
        ArgumentCaptor<CardInfo> captor = ArgumentCaptor.forClass(CardInfo.class);
        verify(cardInfoRepository, times(1)).save(captor.capture());
        CardInfo savedCardInfo = captor.getValue();

        assertThat(savedCardInfo.getUser()).isEqualTo(user);
        assertThat(user.getCards()).contains(savedCardInfo);
        assertThat(user.getCards()).hasSize(1);

        verify(userRepository, times(1)).findById(1L);
        verify(cardInfoMapper, times(1)).toEntity(cardInfoRequestDto1);
        verify(cardInfoMapper, times(1)).toDto(cardInfo1);
    }

    @Test
    @DisplayName("Add card to user should throw exception")
    void testAddCardToUserShouldThrowException() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.addCardInfoToUser(100L, cardInfoRequestDto1));
        verify(cardInfoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Create card")
    void testCreateCard() {
        assertThat(user.getCards()).isEmpty();
        when(userRepository.findById(cardInfoWithUserIdRequestDto1.userId())).thenReturn(Optional.of(user));
        when(cardInfoMapper.toEntity(cardInfoWithUserIdRequestDto1)).thenReturn(cardInfo1);
        when(cardInfoRepository.save(any(CardInfo.class))).thenReturn(cardInfo1);
        when(cardInfoMapper.toDto(cardInfo1)).thenReturn(cardInfoResponseDto1);

        CardInfoResponseDto actual = cardInfoService.create(cardInfoWithUserIdRequestDto1);

        assertThat(actual).isNotNull().isEqualTo(cardInfoResponseDto1);
        ArgumentCaptor<CardInfo> captor = ArgumentCaptor.forClass(CardInfo.class);
        verify(cardInfoRepository, times(1)).save(captor.capture());
        CardInfo savedCardInfo = captor.getValue();

        assertThat(savedCardInfo.getUser()).isEqualTo(user);
        assertThat(user.getCards()).hasSize(1);
        assertThat(user.getCards()).contains(savedCardInfo);

        verify(userRepository, times(1)).findById(1L);
        verify(cardInfoMapper, times(1)).toEntity(cardInfoWithUserIdRequestDto1);
        verify(cardInfoMapper, times(1)).toDto(cardInfo1);
    }

    @Test
    @DisplayName("Create card should throw exception")
    void testCreateCardShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.create(cardInfoWithUserIdRequestDto1));
        verify(cardInfoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Update card by id")
    void testUpdateCard() {
        Long cardIdToUpdate = 1L;

        CardInfoWithUserIdRequestDto updateRequestDto = CardInfoWithUserIdRequestDto.builder()
                .userId(1L)
                .number("9988776655443322")
                .holder("UPDATED HOLDER")
                .expirationDate(LocalDate.of(2035, 12, 31))
                .build();

        CardInfoResponseDto expectedResponseDto = CardInfoResponseDto.builder()
                .id(cardIdToUpdate)
                .userId(1L)
                .number("9988776655443322")
                .holder("UPDATED HOLDER")
                .expirationDate(LocalDate.of(2035, 12, 31))
                .build();

        when(cardInfoRepository.findById(cardIdToUpdate)).thenReturn(Optional.of(cardInfo1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardInfoRepository.save(any(CardInfo.class))).thenReturn(cardInfo1);
        when(cardInfoMapper.toDto(cardInfo1)).thenReturn(expectedResponseDto);

        CardInfoResponseDto actualResponse = cardInfoService.updateById(cardIdToUpdate, updateRequestDto);

        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).isEqualTo(expectedResponseDto);
        verify(cardInfoMapper).updateCardInfoFromDto(updateRequestDto, cardInfo1);
        verify(cardInfoRepository).save(cardInfo1);
    }

    @Test
    @DisplayName("Update card by id should throw exception")
    void testUpdateCardShouldThrowException() {
        when(cardInfoRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.updateById(100L, cardInfoWithUserIdRequestDto1));
        verify(cardInfoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Delete card by id")
    void testDeleteCardById() {
        when(cardInfoRepository.findById(2L)).thenReturn(Optional.of(cardInfo2));
        doNothing().when(cardInfoRepository).deleteById(2L);

        cardInfoService.deleteById(2L);

        verify(cardInfoRepository, times(1)).deleteById(2L);
    }

    @Test
    @DisplayName("Delete card by id should throw exception")
    void testDeleteCardByIdShouldThrowException() {
        when(cardInfoRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.deleteById(100L));
        verify(cardInfoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Get card info by user id")
    void testGetCardInfoByUserId() {
        List<CardInfo> cards = List.of(cardInfo1, cardInfo2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardInfoRepository.findByUserId(1L)).thenReturn(cards);
        when(cardInfoMapper.toDto(cardInfo1)).thenReturn(cardInfoResponseDto1);
        when(cardInfoMapper.toDto(cardInfo2)).thenReturn(cardInfoResponseDto2);
        List<CardInfoResponseDto> expected = List.of(cardInfoResponseDto1, cardInfoResponseDto2);

        List<CardInfoResponseDto> actual = cardInfoService.getCardInfoByUserId(1L);

        assertThat(actual).isNotNull().hasSize(2);
        assertEquals(expected, actual);
        verify(cardInfoRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Get card info by user id should throw exception")
    void testGetCardInfoByUserIdShouldThrowException() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardInfoService.getCardInfoByUserId(100L));
    }
}
