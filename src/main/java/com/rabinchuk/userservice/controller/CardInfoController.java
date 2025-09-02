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
    public ResponseEntity<List<CardInfoResponseDto>> getAll() {
        return ResponseEntity.ok(cardInfoService.getAll());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CardInfoResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cardInfoService.getById(id));
    }

    @GetMapping(value = "/getByIds", params = "ids")
    public ResponseEntity<List<CardInfoResponseDto>> getByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(cardInfoService.getByIds(ids));
    }

    @PostMapping()
    public ResponseEntity<CardInfoResponseDto> create(@Valid @RequestBody CardInfoWithUserIdRequestDto u) {
        return new ResponseEntity<>(cardInfoService.create(u), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardInfoResponseDto> updateById(@PathVariable Long id, @Valid @RequestBody CardInfoWithUserIdRequestDto u) {
        return ResponseEntity.ok(cardInfoService.updateById(id, u));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        cardInfoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addCardInfoToUser/{userId}")
    public ResponseEntity<CardInfoResponseDto> addCardInfoToUser(@PathVariable("userId") Long userId, @Valid @RequestBody CardInfoRequestDto cardInfoRequestDto) {
        return new ResponseEntity<>(cardInfoService.addCardInfoToUser(userId, cardInfoRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/getCardInfoByUserId/{userId}")
    public ResponseEntity<List<CardInfoResponseDto>> getCardInfoByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cardInfoService.getCardInfoByUserId(userId));
    }
}
