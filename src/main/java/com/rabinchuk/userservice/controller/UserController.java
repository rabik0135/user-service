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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasRole('ADMIN') or hasRole('INTERNAL_SERVICE')")
    public ResponseEntity<List<UserResponseDto>> getByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.getByIds(ids));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN') or hasRole('INTERNAL_SERVICE')")
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto u) {
        return new ResponseEntity<>(userService.create(u), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userServiceImpl.getById(#id).email() == authentication.name")
    public ResponseEntity<UserResponseDto> updateById(@PathVariable("id") Long id, @Valid @RequestBody UserRequestDto u) {
        return ResponseEntity.ok(userService.updateById(id, u));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INTERNAL_SERVICE')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/getByEmail/{email}")
    @PreAuthorize("hasRole('ADMIN') or #email == authentication.name or hasRole('INTERNAL_SERVICE')")
    public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

}
