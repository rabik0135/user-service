package com.rabinchuk.userservice.service.impl;

import com.rabinchuk.userservice.dto.UserRequestDto;
import com.rabinchuk.userservice.dto.UserResponseDto;
import com.rabinchuk.userservice.mapper.UserMapper;
import com.rabinchuk.userservice.model.User;
import com.rabinchuk.userservice.repository.UserRepository;
import com.rabinchuk.userservice.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + id)
        ));
    }

    @Override
    public List<UserResponseDto> getByIds(List<Long> ids) {
        return userRepository.findAllById(ids).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto create(UserRequestDto userRequestDto) {
        User user = userMapper.toEntity(userRequestDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto updateById(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + id)
        );
        userMapper.updateUserFromDto(userRequestDto, user);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        return userMapper.toDto(userRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found with email: " + email)
        ));
    }
}
