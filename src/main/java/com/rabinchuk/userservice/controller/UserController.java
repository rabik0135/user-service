package com.rabinchuk.userservice.controller;

import com.rabinchuk.userservice.controller.api.UserApi;
import com.rabinchuk.userservice.dto.UserRequestDto;
import com.rabinchuk.userservice.dto.UserResponseDto;
import com.rabinchuk.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.getById(#id).email() == authentication.name")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping(value = "/getByIds", params = "ids")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.getByIds(ids));
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto u) {
        return new ResponseEntity<>(userService.create(u), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.getById(#id).email() == authentication.name")
    public ResponseEntity<UserResponseDto> updateById(@PathVariable("id") Long id, @Valid @RequestBody UserRequestDto u) {
        return ResponseEntity.ok(userService.updateById(id, u));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/getByEmail/{email}")
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.name")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

}
