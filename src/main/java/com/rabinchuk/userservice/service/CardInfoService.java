package com.rabinchuk.userservice.service;

import com.rabinchuk.userservice.dto.CardInfoRequestDto;
import com.rabinchuk.userservice.dto.CardInfoResponseDto;
import com.rabinchuk.userservice.dto.CardInfoWithUserIdRequestDto;

import java.util.List;

public interface CardInfoService extends CRUDService<CardInfoResponseDto, CardInfoWithUserIdRequestDto> {

    CardInfoResponseDto addCardInfoToUser(Long userId, CardInfoRequestDto cardInfoRequestDto);

    List<CardInfoResponseDto> getCardInfoByUserId(Long userId);
}
