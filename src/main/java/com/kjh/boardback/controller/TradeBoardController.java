package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.trade_board.PatchTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PatchTradeCommentRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeCommentRequestDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeBoardListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeBoardResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeCommentListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.service.TradeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<ResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetTradeFavoriteListResponseDto response = boardService.getFavoriteList(boardNumber);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<ResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetTradeCommentListResponseDto response = boardService.getCommentList(boardNumber);
        return ResponseEntity.ok(ResponseDto.success(response));
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

    @PostMapping("/{boardNumber}/comment")
    public ResponseEntity<ResponseDto> postComment(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostTradeCommentRequestDto dto
    ) {
        boardService.postComment(boardNumber, email, dto);
        return ResponseEntity.ok().body(ResponseDto.success());
    }


    @PutMapping("/{boardNumber}/favorite")
    public ResponseEntity<ResponseDto> putFavorite(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ) {
        boardService.putFavorite(email, boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PatchMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> patchComment(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchTradeCommentRequestDto dto,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber
    ) {
        boardService.patchComment(boardNumber, commentNumber, email, dto);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @DeleteMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> deleteComment(
            @AuthenticationPrincipal String email,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber
    ) {
        boardService.deleteComment(boardNumber, email, commentNumber);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

}
