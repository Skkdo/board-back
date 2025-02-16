package com.kjh.boardback.domain.trade_board.controller;

import com.kjh.boardback.domain.trade_board.dto.response.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.domain.trade_board.service.TradeFavoriteService;
import com.kjh.boardback.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trade/trade-board")
public class TradeFavoriteController {

    private final TradeFavoriteService boardService;

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<ResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetTradeFavoriteListResponseDto response = boardService.getFavoriteList(boardNumber);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @PutMapping("/{boardNumber}/favorite")
    public ResponseEntity<ResponseDto> putFavorite(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ) {
        boardService.putFavorite(email, boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success());
    }
}
