package com.rabinchuk.userservice.mapper;

import com.rabinchuk.userservice.dto.UserRequestDto;
import com.rabinchuk.userservice.dto.UserResponseDto;
import com.rabinchuk.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CardInfoMapper.class})
public interface UserMapper {

    UserResponseDto toDto(User user);

    @Mapping(target = "cards", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDto userRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", ignore = true)
    void updateUserFromDto(UserRequestDto userRequestDto, @MappingTarget User user);
}
