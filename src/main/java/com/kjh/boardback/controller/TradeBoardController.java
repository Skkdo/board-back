package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.trade_board.PatchTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeBoardRequestDto;
import com.kjh.boardback.dto.response.trade_board.*;
import com.kjh.boardback.service.TradeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trade-board")
public class TradeBoardController {

    private final TradeBoardService tradeBoardService;

    @GetMapping("/latest-list")
    public ResponseEntity<? super GetLatestTradeBoardListResponseDto> getTradeBoardList(){
        ResponseEntity<? super GetLatestTradeBoardListResponseDto> response = tradeBoardService.getTradeBoardList();
        return response;
    }

    @GetMapping("/{boardNumber}")
    public ResponseEntity<? super GetTradeBoardResponseDto> getTradeBoard(
        @PathVariable("boardNumber") Integer boardNumber
    ){
        ResponseEntity<? super GetTradeBoardResponseDto> response = tradeBoardService.getTradeBoard(boardNumber);
        return response;
    }

    @PostMapping("")
    public ResponseEntity<? super PostTradeBoardResponseDto> postTradeBoard(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostTradeBoardRequestDto requestDto
            ){
        ResponseEntity<? super PostTradeBoardResponseDto> response = tradeBoardService.postTradeBoard(requestDto, email);
        return response;
    }

    @PatchMapping("/{boardNumber}")
    public ResponseEntity<? super PatchTradeBoardResponseDto> patchTradeBoard(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchTradeBoardRequestDto requestDto,
            @PathVariable("boardNumber") Integer boardNumber
            ){
        ResponseEntity<? super PatchTradeBoardResponseDto> response = tradeBoardService.patchTradeBoard(requestDto, email, boardNumber);
        return response;
    }

    @DeleteMapping("/{boardNumber}")
    public ResponseEntity<? super DeleteTradeBoardResponseDto> deleteTradeBoard(
            @AuthenticationPrincipal String email,
            @PathVariable("boardNumber") Integer boardNumber
    ){
        ResponseEntity<? super DeleteTradeBoardResponseDto> response = tradeBoardService.deleteTradeBoard(email, boardNumber);
        return response;
    }

}
