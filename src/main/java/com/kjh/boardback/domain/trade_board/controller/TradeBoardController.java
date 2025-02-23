package com.kjh.boardback.domain.trade_board.controller;

import com.kjh.boardback.domain.trade_board.dto.request.PatchTradeBoardRequestDto;
import com.kjh.boardback.domain.trade_board.dto.request.PostTradeBoardRequestDto;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeBoardListResponseDto;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeBoardResponseDto;
import com.kjh.boardback.domain.trade_board.service.TradeBoardService;
import com.kjh.boardback.global.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trade/trade-board")
public class TradeBoardController {

    private final TradeBoardService boardService;

    @GetMapping("/latest-list")
    public ResponseEntity<ResponseDto> getLatestBoardList() {
        GetTradeBoardListResponseDto response = boardService.getLatestBoardList();
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> getBoard(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetTradeBoardResponseDto response = boardService.getBoard(boardNumber);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto> postBoard(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostTradeBoardRequestDto requestDto
    ) {
        boardService.postBoard(requestDto, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PatchMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> patchBoard(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchTradeBoardRequestDto requestDto,
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.patchBoard(requestDto, boardNumber, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @DeleteMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> deleteBoard(
            @AuthenticationPrincipal String email,
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.deleteBoard(boardNumber, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @GetMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<ResponseDto> increaseViewCount(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.increaseViewCount(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @GetMapping("/top-3")
    public ResponseEntity<ResponseDto> getTop3BoardList() {
        GetTradeBoardListResponseDto response = boardService.getTop3BoardList();
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping(value = {"/search-list/{searchWord}", "/search-list/{searchWord}/{preSearchWord}"})
    public ResponseEntity<ResponseDto> getSearchBoardList(
            @PathVariable("searchWord") String searchWord,
            @PathVariable(value = "preSearchWord", required = false) String PreSearchWord
    ) {
        GetTradeBoardListResponseDto response = boardService.getSearchBoardList(searchWord, PreSearchWord);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/user-board-list/{email}")
    public ResponseEntity<ResponseDto> getUserBoardList(
            @PathVariable("email") String email
    ) {
        GetTradeBoardListResponseDto response = boardService.getUserBoardList(email);
        return ResponseEntity.ok(ResponseDto.success(response));
    }
}
