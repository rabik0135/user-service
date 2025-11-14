package com.rabinchuk.userservice.controller.api;

import com.rabinchuk.userservice.dto.ErrorResponseDto;
import com.rabinchuk.userservice.dto.UserRequestDto;
import com.rabinchuk.userservice.dto.UserResponseDto;
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

@Tag(name = "User Management", description = "APIs for creating, retrieving, updating, and deleting users")
public interface UserApi {

    @Operation(summary = "Get all users", description = "Returns a list of all users in the system.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    ResponseEntity<List<UserResponseDto>> getAll();

    @Operation(summary = "Get user by ID", description = "Returns a single user by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<UserResponseDto> getById(
            @Parameter(description = "ID of the user to be retrieved", required = true, example = "1") @PathVariable Long id);

    @Operation(summary = "Get multiple users by their IDs", description = "Returns a list of users for a given list of IDs.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    ResponseEntity<List<UserResponseDto>> getByIds(
            @Parameter(description = "A comma-separated list of user IDs", required = true, example = "1,2,3") @RequestParam List<Long> ids);

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto u);

    @Operation(summary = "Update an existing user", description = "Updates the details of an existing user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<UserResponseDto> updateById(
            @Parameter(description = "ID of the user to be updated", required = true, example = "1") @PathVariable("id") Long id,
            @Valid @RequestBody UserRequestDto u);

    @Operation(summary = "Delete a user by ID", description = "Deletes a user from the system by their unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<Void> deleteById(
            @Parameter(description = "ID of the user to be deleted", required = true, example = "1") @PathVariable Long id);

    @Operation(summary = "Get user by email", description = "Returns a single user by their email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    ResponseEntity<UserResponseDto> getUserByEmail(
            @Parameter(description = "Email address of the user to be retrieved", required = true, example = "john.doe@example.com") @PathVariable String email);
}
