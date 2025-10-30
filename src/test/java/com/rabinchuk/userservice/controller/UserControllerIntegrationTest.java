package com.rabinchuk.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabinchuk.userservice.AbstractIntegrationTest;
import com.rabinchuk.userservice.dto.UserRequestDto;
import com.rabinchuk.userservice.model.User;
import com.rabinchuk.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/cleanup_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @MockitoSpyBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CacheManager cacheManager;

    User existingUser;

    @BeforeEach
    public void setup() {
        cacheManager.getCacheNames()
                .forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());

        existingUser = userRepository.save(User.builder()
                .name("Michael")
                .surname("Jordan")
                .birthDate(LocalDate.of(1963, 2, 17))
                .email("michael.jordan@gmail.com")
                .build());
    }

    @Nested
    @DisplayName("Happy scenarios")
    public class HappyScenarios {
        @Test
        @DisplayName("Get all users")
        public void testGetAllUsers() throws Exception {
            User existingUser2 = userRepository.save(User.builder()
                    .name("Michael")
                    .surname("Jackson")
                    .birthDate(LocalDate.of(1958, 8, 29))
                    .email("michael.jackson@gmail.com")
                    .build()
            );

            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/get_all_users_response.json")));
        }

        @Test
        @DisplayName("Get users by ids")
        public void testGetUserByIds() throws Exception {
            User existingUser2 = userRepository.save(User.builder()
                    .name("Michael")
                    .surname("Jackson")
                    .birthDate(LocalDate.of(1958, 8, 29))
                    .email("michael.jackson@gmail.com")
                    .build()
            );

            mockMvc.perform(get("/api/users/getByIds?ids=1&ids=2"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/get_all_users_response.json")));
        }

        @Test
        @DisplayName("Get user by id")
        public void testGetUserById() throws Exception {
            Long userId = existingUser.getId();

            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/get_user_by_id_response.json")));
        }

        @Test
        @DisplayName("Create user")
        public void testCreateUser() throws Exception {
            UserRequestDto userRequestDto = UserRequestDto.builder()
                    .name("Michael")
                    .surname("Jackson")
                    .birthDate(LocalDate.of(1958, 8, 29))
                    .email("michael.jackson@gmail.com")
                    .build();

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/create_user_response.json")));
        }

        @Test
        @DisplayName("Update user")
        public void testUpdateUser() throws Exception {
            Long userId = existingUser.getId();

            UserRequestDto userRequestDto = UserRequestDto.builder()
                    .name("John")
                    .surname("Doe")
                    .birthDate(LocalDate.of(1999, 9, 19))
                    .email("john.doe@gmail.com")
                    .build();

            mockMvc.perform(put("/api/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/update_user_response.json")));
        }

        @Test
        @DisplayName("Delete user by id")
        public void testDeleteUser() throws Exception {
            Long userId = existingUser.getId();

            mockMvc.perform(delete("/api/users/{id}", userId))
                    .andExpect(status().isNoContent());
            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Get user by email")
        public void testGetUserByEmail() throws Exception {
            String email = existingUser.getEmail();

            mockMvc.perform(get("/api/users/getByEmail/{email}", email))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/get_user_by_id_response.json")));
        }
    }

    @Nested
    @DisplayName("Error scenarios")
    class ErrorScenarios {
        @Test
        @DisplayName("Get all users should return empty list")
        public void testGetAllUsersShouldReturnEmptyList() throws Exception {
            userRepository.deleteAll();
            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/blank_list_response.json")));
        }

        @Test
        @DisplayName("Get user by ids should return only one user")
        public void testGetUserByIdsShouldReturnOnlyOne() throws Exception {
            mockMvc.perform(get("/api/users/getByIds?ids=1&ids=2"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/errors/get_users_by_ids_should_return_only_one_response.json")));
        }

        @Test
        @DisplayName("Get user by id should return error")
        public void testGetUserByIdShouldReturnError() throws Exception {
            mockMvc.perform(get("/api/users/getById/{id}", 2))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/errors/get_user_by_id_error_response.json"), false));
        }

        @Test
        @DisplayName("Create user should return error")
        public void testCreateUserShouldReturnError() throws Exception {
            UserRequestDto userRequestDto = UserRequestDto.builder()
                    .name("string")
                    .surname("string")
                    .birthDate(LocalDate.of(2026, 9, 1))
                    .email("string")
                    .build();

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/errors/create_user_error_response.json"), false));
        }

        @Test
        @DisplayName("Update user with invalid id should return error")
        public void testUpdateUserWithInvalidIdShouldReturnError() throws Exception {
            UserRequestDto userRequestDto = UserRequestDto.builder()
                    .name("John")
                    .surname("Doe")
                    .birthDate(LocalDate.of(2016, 9, 1))
                    .email("john.doe@gmail.com")
                    .build();

            mockMvc.perform(put("/api/users/{id}", 248)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/errors/update_user_id_error_response.json"), false));
        }

        @Test
        @DisplayName("Update user with invalid fields should return error")
        public void testUpdateUserWithInvalidFieldsShouldReturnError() throws Exception {
            UserRequestDto userRequestDto = UserRequestDto.builder()
                    .name("string")
                    .surname("string")
                    .birthDate(LocalDate.of(2026, 9, 1))
                    .email("string")
                    .build();

            mockMvc.perform(put("/api/users/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/errors/update_user_fields_error_response.json"), false));
        }

        @Test
        @DisplayName("Delete user with invalid id should return error")
        public void testDeleteUserWithInvalidIdShouldReturnError() throws Exception {
            mockMvc.perform(delete("/api/users/{id}", 76))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/errors/delete_user_by_id_error_response.json"), false));
        }


        @Test
        @DisplayName("Get user by email should return error")
        public void testGetUserByEmailShouldReturnError() throws Exception {
            String email = "john.doe@gmail.com";
            mockMvc.perform(get("/api/users/getByEmail/{email}", email))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/errors/get_user_by_email_error_response.json"), false));
        }
    }

    @Nested
    @DisplayName("Caching tests")
    class GetAllUsers {
        @Test
        @DisplayName("Get user by id cache check")
        public void testGetUserByIdCacheCheck() throws Exception {
            User cachingUser = userRepository.save(User.builder()
                    .name("Michael")
                    .surname("Jackson")
                    .birthDate(LocalDate.of(1958, 8, 29))
                    .email("michael.jackson@gmail.com")
                    .build()
            );
            Long userId = cachingUser.getId();

            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isOk());
            Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
        }

        @Test
        @DisplayName("Update user cache check")
        public void testUpdateUserCacheCheck() throws Exception {
            Long userId = existingUser.getId();

            UserRequestDto userRequestDto = UserRequestDto.builder()
                    .name("John")
                    .surname("Doe")
                    .birthDate(LocalDate.of(1999, 9, 19))
                    .email("john.doe@gmail.com")
                    .build();

            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isOk());
            mockMvc.perform(put("/api/users/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequestDto)))
                    .andExpect(status().isOk());
            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/user/update_user_response.json")));

            Mockito.verify(userRepository, Mockito.times(2)).findById(userId);
        }

        @Test
        @DisplayName("Delete user cache check")
        public void testDeleteUserCacheCheck() throws Exception {
            Long userId = existingUser.getId();

            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isOk());
            mockMvc.perform(delete("/api/users/{id}", userId))
                    .andExpect(status().isNoContent());
            mockMvc.perform(get("/api/users/getById/{id}", userId))
                    .andExpect(status().isNotFound());

            Mockito.verify(userRepository, Mockito.times(3)).findById(userId);
        }
    }
}
