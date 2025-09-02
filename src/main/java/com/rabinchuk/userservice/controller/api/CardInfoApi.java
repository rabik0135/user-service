package com.rabinchuk.userservice.controller.api;

import com.rabinchuk.userservice.dto.CardInfoRequestDto;
import com.rabinchuk.userservice.dto.CardInfoResponseDto;
import com.rabinchuk.userservice.dto.CardInfoWithUserIdRequestDto;
import com.rabinchuk.userservice.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Card Management", description = "APIs for managing user payment cards")
public interface CardInfoApi {

    @Operation(summary = "Get all cards", description = "Returns a list of all cards in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of cards")
    ResponseEntity<List<CardInfoResponseDto>> getAll();

    @Operation(summary = "Get card by ID", description = "Returns a single card by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInfoResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<CardInfoResponseDto> getById(
            @Parameter(description = "ID of the card to be retrieved", required = true, example = "1") @PathVariable Long id);

    @Operation(summary = "Get multiple cards by their IDs", description = "Returns a list of cards for a given list of IDs.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved cards")
    ResponseEntity<List<CardInfoResponseDto>> getByIds(
            @Parameter(description = "A comma-separated list of card IDs", required = true, example = "1,2,3") @RequestParam List<Long> ids);

    @Operation(summary = "Create a new card", description = "Creates a new card with the provided data and associates it with a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInfoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User specified by userId not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<CardInfoResponseDto> create(@Valid @RequestBody CardInfoWithUserIdRequestDto u);

    @Operation(summary = "Update an existing card", description = "Updates the details of an existing card by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Card updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInfoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Card or User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<CardInfoResponseDto> updateById(
            @Parameter(description = "ID of the card to be updated", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody CardInfoWithUserIdRequestDto u);

    @Operation(summary = "Delete a card by ID", description = "Deletes a card from the system by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<Void> deleteById(
            @Parameter(description = "ID of the card to be deleted", required = true, example = "1") @PathVariable Long id);

    @Operation(summary = "Add a new card to a specific user", description = "Creates a new card and associates it with the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card added to user successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInfoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid card data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<CardInfoResponseDto> addCardInfoToUser(
            @Parameter(description = "ID of the user to whom the card will be added", required = true, example = "1") @PathVariable("userId") Long userId,
            @Valid @RequestBody CardInfoRequestDto cardInfoRequestDto);

    @Operation(summary = "Get all cards for a specific user", description = "Returns a list of all cards associated with a given user ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's cards"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<List<CardInfoResponseDto>> getCardInfoByUserId(
            @Parameter(description = "ID of the user whose cards are to be retrieved", required = true, example = "1") @PathVariable Long userId);
}
