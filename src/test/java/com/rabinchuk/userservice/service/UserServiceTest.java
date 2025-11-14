package com.rabinchuk.userservice.service;

import com.rabinchuk.userservice.dto.UserRequestDto;
import com.rabinchuk.userservice.dto.UserResponseDto;
import com.rabinchuk.userservice.mapper.UserMapper;
import com.rabinchuk.userservice.model.User;
import com.rabinchuk.userservice.repository.UserRepository;
import com.rabinchuk.userservice.service.impl.UserServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;
    private UserResponseDto userResponseDto1;
    private UserRequestDto userRequestDto1;

    private User user2;
    private UserResponseDto userResponseDto2;

    @BeforeEach
    void setUp() {
        user1 = TestDataFactory.createUser1();

        userResponseDto1 = TestDataFactory.createUserResponseDto1();

        userRequestDto1 = TestDataFactory.createUserRequestDto1();

        user2 = TestDataFactory.createUser2();

        userResponseDto2 = TestDataFactory.createUserResponseDto2();
    }

    @Test
    @DisplayName("Get all users")
    void testGetAllUsers() {
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(user1)).thenReturn(userResponseDto1);
        when(userMapper.toDto(user2)).thenReturn(userResponseDto2);
        List<UserResponseDto> expected = List.of(userResponseDto1, userResponseDto2);

        List<UserResponseDto> result = userService.getAll();

        assertThat(result).isNotNull().hasSize(2);
        assertEquals(expected, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get all users should return empty list")
    void testGetAllUsersWithEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDto> result = userService.getAll();

        assertThat(result).isEmpty();
        verify(userRepository).findAll();
        verify(userMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Get user by id")
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userMapper.toDto(user1)).thenReturn(userResponseDto1);

        UserResponseDto result = userService.getById(1L);

        assertThat(result).isNotNull().isEqualTo(userResponseDto1);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Get user by id should throw exception")
    void testGetUserByIdWithInvalidId() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getById(100L));
    }

    @Test
    @DisplayName("Get users by ids")
    void testGetUserByIds() {
        when(userRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(userResponseDto1);
        when(userMapper.toDto(user2)).thenReturn(userResponseDto2);
        List<UserResponseDto> expected = List.of(userResponseDto1, userResponseDto2);

        List<UserResponseDto> result = userService.getByIds(List.of(1L, 2L));

        assertThat(result).isNotNull().hasSize(2);
        assertEquals(expected, result);

        verify(userRepository).findAllById(List.of(1L, 2L));
    }

    @Test
    @DisplayName("Create user")
    void testCreateUser() {
        when(userMapper.toEntity(userRequestDto1)).thenReturn(user1);
        when(userRepository.save(user1)).thenReturn(user1);
        when(userMapper.toDto(user1)).thenReturn(userResponseDto1);

        UserResponseDto result = userService.create(userRequestDto1);

        assertThat(result).isNotNull().isEqualTo(userResponseDto1);
        verify(userRepository).save(user1);
    }

    @Test
    @DisplayName("Update user")
    void testUpdateUser() {
        UserRequestDto userUpdateDto = UserRequestDto.builder()
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1958, 8, 29))
                .email("test.email@gmail.com")
                .build();

        UserResponseDto expectedResponse = UserResponseDto.builder()
                .id(1L)
                .name("John")
                .surname("Doe")
                .birthDate(LocalDate.of(1958, 8, 29))
                .email("test.email@gmail.com")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);
        when(userMapper.toDto(user1)).thenReturn(expectedResponse);

        UserResponseDto actualResponse = userService.updateById(1L, userUpdateDto);

        assertThat(actualResponse).isNotNull().isEqualTo(expectedResponse);
        assertThat(actualResponse.name()).isEqualTo(expectedResponse.name());

        verify(userRepository).findById(1L);
        verify(userMapper).updateUserFromDto(userUpdateDto, user1);
        verify(userRepository).save(user1);
    }

    @Test
    @DisplayName("Update user should throw exception")
    void testUpdateUserWithInvalidId() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.updateById(100L, userRequestDto1));
    }

    @Test
    @DisplayName("Delete user by id")
    void testDeleteUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete user by id should throw exception")
    void testDeleteUserByIdWithInvalidId() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteById(100L));
        verify(userRepository, never()).deleteById(100L);
    }

    @Test
    @DisplayName("Get user by email")
    void testGetUserByEmail() {
        String email = "michael.jordan@gmail.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user1));
        when(userMapper.toDto(user1)).thenReturn(userResponseDto1);

        UserResponseDto result = userService.getUserByEmail(email);

        assertThat(result).isNotNull().isEqualTo(userResponseDto1);
    }

    @Test
    @DisplayName("Get user by email should throw exception")
    void testGetUserByInvalidEmail() {
        when(userRepository.findUserByEmail("invalid.email")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail("invalid.email"));
    }
}
