package com.rabinchuk.userservice.controller;

import com.rabinchuk.userservice.controller.api.CardInfoApi;
import com.rabinchuk.userservice.dto.CardInfoRequestDto;
import com.rabinchuk.userservice.dto.CardInfoResponseDto;
import com.rabinchuk.userservice.dto.CardInfoWithUserIdRequestDto;
import com.rabinchuk.userservice.service.CardInfoService;
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
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardInfoController implements CardInfoApi {

    private final CardInfoService cardInfoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CardInfoResponseDto>> getAll() {
        return ResponseEntity.ok(cardInfoService.getAll());
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasRole('ADMIN') or @cardInfoServiceImpl.getById(#id).userId() == @userServiceImpl.getUserByEmail(authentication.name).id()")
    public ResponseEntity<CardInfoResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.getById(id));
    }

    @GetMapping(value = "/getByIds", params = "ids")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CardInfoResponseDto>> getByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(cardInfoService.getByIds(ids));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardInfoResponseDto> create(@Valid @RequestBody CardInfoWithUserIdRequestDto u) {
        return new ResponseEntity<>(cardInfoService.create(u), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @cardInfoServiceImpl.getById(#id).userId().equals(@userServiceImpl.getUserByEmail(authentication.name).id())")
    public ResponseEntity<CardInfoResponseDto> updateById(@PathVariable Long id, @Valid @RequestBody CardInfoWithUserIdRequestDto u) {
        return ResponseEntity.ok(cardInfoService.updateById(id, u));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @cardInfoServiceImpl.getById(#id).userId().equals(@userServiceImpl.getUserByEmail(authentication.name).id())")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        cardInfoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addCardInfoToUser/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == @userServiceImpl.getUserByEmail(authentication.name).id()")
    public ResponseEntity<CardInfoResponseDto> addCardInfoToUser(@PathVariable("userId") Long userId, @Valid @RequestBody CardInfoRequestDto cardInfoRequestDto) {
        return new ResponseEntity<>(cardInfoService.addCardInfoToUser(userId, cardInfoRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/getCardInfoByUserId/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == @userServiceImpl.getUserByEmail(authentication.name).id()")
    public ResponseEntity<List<CardInfoResponseDto>> getCardInfoByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cardInfoService.getCardInfoByUserId(userId));
    }

}
