package com.rabinchuk.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabinchuk.userservice.AbstractIntegrationTest;
import com.rabinchuk.userservice.dto.CardInfoRequestDto;
import com.rabinchuk.userservice.dto.CardInfoWithUserIdRequestDto;
import com.rabinchuk.userservice.model.CardInfo;
import com.rabinchuk.userservice.model.User;
import com.rabinchuk.userservice.repository.CardInfoRepository;
import com.rabinchuk.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/cleanup_cards.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/cleanup_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CardInfoControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CardInfoRepository cardInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    User existingUser;
    CardInfo cardInfo;

    @BeforeEach
    public void setUp() {
        existingUser = userRepository.save(User.builder()
                .name("Michael")
                .surname("Jordan")
                .birthDate(LocalDate.of(1963, 2, 17))
                .email("michael.jordan@gmail.com")
                .build());

        cardInfo = cardInfoRepository.save(CardInfo.builder()
                .user(existingUser)
                .number("4916989612345678")
                .holder("MICHAELJORDAN")
                .expirationDate(LocalDate.of(2028, 1, 20))
                .build());
    }

    @Nested
    @DisplayName("Happy scenarios")
    public class HappyScenarios {
        @Test
        @DisplayName("Get all cards")
        public void testGetAllCards() throws Exception {
            CardInfo cardInfo2 = cardInfoRepository.save(CardInfo.builder()
                    .user(existingUser)
                    .number("4916989687654321")
                    .holder("MICHAELJORDAN")
                    .expirationDate(LocalDate.of(2030, 1, 20))
                    .build());

            mockMvc.perform(get("/api/cards"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/get_all_cards_response.json")));

        }

        @Test
        @DisplayName("Get card by id")
        public void testGetCardById() throws Exception {
            Long userId = existingUser.getId();
            mockMvc.perform(get("/api/cards/getById/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/get_card_by_id_response.json")));
        }

        @Test
        @DisplayName("Get cards by ids")
        public void testGetCardByIds() throws Exception {
            CardInfo cardInfo2 = cardInfoRepository.save(CardInfo.builder()
                    .user(existingUser)
                    .number("4916989687654321")
                    .holder("MICHAELJORDAN")
                    .expirationDate(LocalDate.of(2030, 1, 20))
                    .build());

            mockMvc.perform(get("/api/cards/getByIds?ids=1&ids=2"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/get_all_cards_response.json")));
        }

        @Test
        @DisplayName("Create card")
        public void testCreateCard() throws Exception {
            CardInfoWithUserIdRequestDto cardInfoDto = CardInfoWithUserIdRequestDto.builder()
                    .userId(1L)
                    .number("4916989687654321")
                    .holder("MICHAELJORDAN")
                    .expirationDate(LocalDate.of(2030, 1, 20))
                    .build();

            mockMvc.perform(post("/api/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(cardInfoDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/create_card_response.json")));
        }

        @Test
        @DisplayName("Update card")
        public void testUpdateCard() throws Exception {
            CardInfoWithUserIdRequestDto updateCardInfoDto = CardInfoWithUserIdRequestDto.builder()
                    .userId(1L)
                    .number("1122334455667788")
                    .holder("MICHAELBJORDAN")
                    .expirationDate(LocalDate.of(2029, 1, 20))
                    .build();
            Long cardId = cardInfo.getId();

            mockMvc.perform(put("/api/cards/{id}", cardId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updateCardInfoDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/update_card_response.json")));
        }

        @Test
        @DisplayName("Delete card")
        public void testDeleteCard() throws Exception {
            Long cardId = cardInfo.getId();

            mockMvc.perform(delete("/api/cards/{id}", cardId))
                    .andExpect(status().isNoContent());
            mockMvc.perform(get("/api/cards/getById/{id}", cardId))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Add card info to user")
        public void testAddCardToUser() throws Exception {
            CardInfoRequestDto cardInfoDto = CardInfoRequestDto.builder()
                    .number("4916989687654321")
                    .holder("MICHAELJORDAN")
                    .expirationDate(LocalDate.of(2030, 1, 20))
                    .build();
            Long userId = existingUser.getId();

            mockMvc.perform(post("/api/cards/addCardInfoToUser/{id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(cardInfoDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/create_card_response.json")));
        }

        @Test
        @DisplayName("Get card info by user id")
        public void testGetCardByUserId() throws Exception {
            CardInfo cardInfo2 = cardInfoRepository.save(CardInfo.builder()
                    .user(existingUser)
                    .number("4916989687654321")
                    .holder("MICHAELJORDAN")
                    .expirationDate(LocalDate.of(2030, 1, 20))
                    .build());
            Long userId = existingUser.getId();

            mockMvc.perform(get("/api/cards/getCardInfoByUserId/{id}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/get_all_cards_response.json")));
        }
    }

    @Nested
    @DisplayName("Error scenarios")
    public class ErrorScenarios {
        @Test
        @DisplayName("Get all cards should return empty list")
        public void testGetAllCardsShouldReturnEmptyList() throws Exception {
            cardInfoRepository.deleteAll();
            mockMvc.perform(get("/api/cards"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/blank_list_response.json")));
        }

        @Test
        @DisplayName("Get card by id should return error")
        public void testGetCardByIdShouldReturnError() throws Exception {
            mockMvc.perform(get("/api/cards/getById/{id}", 100))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/get_card_by_id_error_response.json"), false));
        }

        @Test
        @DisplayName("Get cards by ids should return only one")
        public void testGetCardByIdsShouldReturnOnlyOne() throws Exception {
            mockMvc.perform(get("/api/cards"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/get_cards_by_ids_should_return_only_one_response.json")));
        }

        @Test
        @DisplayName("Create card with invalid id should return error")
        public void testCreateCardWithInvalidIdShouldReturnError() throws Exception {
            CardInfoWithUserIdRequestDto cardInfoDto = CardInfoWithUserIdRequestDto.builder()
                    .userId(100L)
                    .number("4916989687654321")
                    .holder("MICHAELJORDAN")
                    .expirationDate(LocalDate.of(2030, 1, 20))
                    .build();

            mockMvc.perform(post("/api/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(cardInfoDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/create_card_id_error_response.json"), false));
        }

        @Test
        @DisplayName("Create card with invalid fields should return error")
        public void testCreateCardWithInvalidFieldsShouldReturnError() throws Exception {
            CardInfoWithUserIdRequestDto cardInfoDto = CardInfoWithUserIdRequestDto.builder()
                    .userId(1L)
                    .number("10")
                    .holder("string")
                    .expirationDate(LocalDate.of(2015, 9, 2))
                    .build();

            mockMvc.perform(post("/api/cards")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(cardInfoDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/create_card_fields_error_response.json"), false));
        }

        @Test
        @DisplayName("Update card should return error")
        public void testUpdateCardWithInvalidIdShouldReturnError() throws Exception {
            CardInfoWithUserIdRequestDto updateCardInfoDto = CardInfoWithUserIdRequestDto.builder()
                    .userId(100L)
                    .number("1122334455667788")
                    .holder("MICHAELBJORDAN")
                    .expirationDate(LocalDate.of(2029, 1, 20))
                    .build();

            mockMvc.perform(put("/api/cards/{id}", 100L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updateCardInfoDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/update_card_id_error_response.json")));
        }

        @Test
        @DisplayName("Update card should return error")
        public void testUpdateCardWithInvalidFieldsShouldReturnError() throws Exception {
            CardInfoWithUserIdRequestDto updateCardInfoDto = CardInfoWithUserIdRequestDto.builder()
                    .userId(100L)
                    .number("10")
                    .holder("string")
                    .expirationDate(LocalDate.of(2016, 9, 2))
                    .build();

            mockMvc.perform(put("/api/cards/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updateCardInfoDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/update_card_fields_error_response.json")));
        }

        @Test
        @DisplayName("Update card should return error")
        public void testUpdateCardWithInvalidUserIdShouldReturnError() throws Exception {
            CardInfoWithUserIdRequestDto updateCardInfoDto = CardInfoWithUserIdRequestDto.builder()
                    .userId(100L)
                    .number("1122334455667788")
                    .holder("MICHAELBJORDAN")
                    .expirationDate(LocalDate.of(2029, 1, 20))
                    .build();

            mockMvc.perform(put("/api/cards/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(updateCardInfoDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/update_card_user_id_error_response.json")));

        }

        @Test
        @DisplayName("Delete card should return error")
        public void testDeleteCardShouldReturnError() throws Exception {
            mockMvc.perform(delete("/api/cards/{id}", 2342L))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/delete_card_error_response.json"), false));
        }

        @Test
        @DisplayName("Add card info to user with invalid user id should return error")
        public void testAddCardToUserWithInvalidUserIdShouldReturnError() throws Exception {
            CardInfoRequestDto cardInfoRequestDto = CardInfoRequestDto.builder()
                    .number("1122334455667788")
                    .holder("MICHAELBJORDAN")
                    .expirationDate(LocalDate.of(2029, 1, 20))
                    .build();

            mockMvc.perform(post("/api/cards/addCardInfoToUser/{id}", 200L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(cardInfoRequestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/add_card_to_user_id_error_response.json"), false));
        }

        @Test
        @DisplayName("Add card info to user with invalid fields should return error")
        public void testAddCardToUserWithInvalidFieldsShouldReturnError() throws Exception {
            CardInfoRequestDto cardInfoRequestDto = CardInfoRequestDto.builder()
                    .number("10")
                    .holder("MICHAELBJORDAN")
                    .expirationDate(LocalDate.of(2019, 1, 20))
                    .build();

            mockMvc.perform(post("/api/cards/addCardInfoToUser/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(cardInfoRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/add_card_to_user_fields_error_response.json"), false));
        }


        @Test
        @DisplayName("Get card info by user id should return error")
        public void testGetCardByUserId() throws Exception {
            mockMvc.perform(get("/api/cards/getCardInfoByUserId/{id}", 897L))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(AbstractIntegrationTest.readStringFromSource("json/cardInfo/errors/get_card_by_user_id_error_response.json")));
        }
    }
}
