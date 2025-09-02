package com.rabinchuk.userservice.mapper;

import com.rabinchuk.userservice.dto.CardInfoRequestDto;
import com.rabinchuk.userservice.dto.CardInfoResponseDto;
import com.rabinchuk.userservice.dto.CardInfoWithUserIdRequestDto;
import com.rabinchuk.userservice.model.CardInfo;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CardInfoMapper {

    @Mapping(source = "user.id", target = "userId")
    CardInfoResponseDto toDto(CardInfo cardInfo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CardInfo toEntity(CardInfoRequestDto cardInfoRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    CardInfo toEntity(CardInfoWithUserIdRequestDto cardInfoWithUserIdRequestDto);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateCardInfoFromDto(CardInfoWithUserIdRequestDto cardInfoWithUserIdRequestDto, @MappingTarget CardInfo cardInfo);
}

