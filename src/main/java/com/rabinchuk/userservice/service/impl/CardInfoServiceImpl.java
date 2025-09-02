package com.rabinchuk.userservice.service.impl;

import com.rabinchuk.userservice.dto.CardInfoRequestDto;
import com.rabinchuk.userservice.dto.CardInfoResponseDto;
import com.rabinchuk.userservice.dto.CardInfoWithUserIdRequestDto;
import com.rabinchuk.userservice.mapper.CardInfoMapper;
import com.rabinchuk.userservice.model.CardInfo;
import com.rabinchuk.userservice.model.User;
import com.rabinchuk.userservice.repository.CardInfoRepository;
import com.rabinchuk.userservice.repository.UserRepository;
import com.rabinchuk.userservice.service.CardInfoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardInfoServiceImpl implements CardInfoService {

    private final CardInfoRepository cardInfoRepository;
    private final UserRepository userRepository;
    private final CardInfoMapper cardInfoMapper;


    @Override
    public List<CardInfoResponseDto> getAll() {
        return cardInfoRepository.findAll().stream()
                .map(cardInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardInfoResponseDto getById(Long id) {
        return cardInfoMapper.toDto(cardInfoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card Info with id " + id + " not found!")
        ));
    }

    @Override
    public List<CardInfoResponseDto> getByIds(List<Long> ids) {
        return cardInfoRepository.findAllById(ids).stream()
                .map(cardInfoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CardInfoResponseDto addCardInfoToUser(Long userId, CardInfoRequestDto cardInfoRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found!")
        );
        CardInfo cardInfo = cardInfoMapper.toEntity(cardInfoRequestDto);
        user.addCardInfo(cardInfo);

        return cardInfoMapper.toDto(cardInfoRepository.save(cardInfo));
    }

    @Override
    public CardInfoResponseDto create(CardInfoWithUserIdRequestDto cardInfoWithUserIdRequestDto) {
        User user = userRepository.findById(cardInfoWithUserIdRequestDto.userId()).orElseThrow(
                () -> new EntityNotFoundException("User with id " + cardInfoWithUserIdRequestDto.userId() + " not found!")
        );
        CardInfo cardInfo = cardInfoMapper.toEntity(cardInfoWithUserIdRequestDto);
        user.addCardInfo(cardInfo);

        return cardInfoMapper.toDto(cardInfoRepository.save(cardInfo));
    }

    @Override
    @Transactional
    public CardInfoResponseDto updateById(Long id, CardInfoWithUserIdRequestDto cardInfoWithUserIdRequestDto) {
        CardInfo cardInfo = cardInfoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card Info with id " + id + " not found!")
        );
        userRepository.findById(cardInfoWithUserIdRequestDto.userId()).orElseThrow(
                () -> new EntityNotFoundException("User with id " + cardInfoWithUserIdRequestDto.userId() + " not found!")
        );
        cardInfoMapper.updateCardInfoFromDto(cardInfoWithUserIdRequestDto, cardInfo);

        return cardInfoMapper.toDto(cardInfoRepository.save(cardInfo));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cardInfoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Card Info with id " + id + " not found!")
        );

        cardInfoRepository.deleteById(id);
    }

    @Override
    public List<CardInfoResponseDto> getCardInfoByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found!")
        );

        return cardInfoRepository.findByUserId(userId).stream()
                .map(cardInfoMapper::toDto)
                .collect(Collectors.toList());
    }
}
