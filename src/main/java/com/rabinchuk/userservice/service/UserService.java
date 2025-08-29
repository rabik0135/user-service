package com.rabinchuk.userservice.service;

import com.rabinchuk.userservice.dto.UserRequestDto;
import com.rabinchuk.userservice.dto.UserResponseDto;


public interface UserService extends CRUDService<UserResponseDto, UserRequestDto> {

    UserResponseDto getUserByEmail(String email);
}
