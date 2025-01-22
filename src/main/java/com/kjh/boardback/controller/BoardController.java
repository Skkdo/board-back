package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.board.PatchBoardRequestDto;
import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.board.PostBoardRequestDto;
import com.kjh.boardback.dto.request.board.PostCommentRequestDto;
import com.kjh.boardback.dto.response.board.GetBoardListResponseDto;
import com.kjh.boardback.dto.response.board.GetBoardResponseDto;
import com.kjh.boardback.dto.response.board.GetCommentListResponseDto;
import com.kjh.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.service.BoardService;
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
@RequestMapping("/api/v1/community/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> getBoard(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetBoardResponseDto response = boardService.getBoard(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<ResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetFavoriteListResponseDto response = boardService.getFavoriteList(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }


    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<ResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetCommentListResponseDto response = boardService.getCommentList(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @GetMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<ResponseDto> increaseViewCount(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.increaseViewCount(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @GetMapping("/latest-list")
    public ResponseEntity<ResponseDto> getLatestBoardList() {
        GetBoardListResponseDto response = boardService.getLatestBoardList();
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @GetMapping("/top-3")
    public ResponseEntity<ResponseDto> getTop3BoardList() {
        GetBoardListResponseDto response = boardService.getTop3BoardList();
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @GetMapping(value = {"/search-list/{searchWord}", "/search-list/{searchWord}/{preSearchWord}"})
    public ResponseEntity<ResponseDto> getSearchBoardList(
            @PathVariable("searchWord") String searchWord,
            @PathVariable(value = "preSearchWord", required = false) String PreSearchWord
    ) {
        GetBoardListResponseDto response = boardService.getSearchBoardList(searchWord, PreSearchWord);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/user-board-list/{email}")
    public ResponseEntity<ResponseDto> getUserBoardList(
            @PathVariable("email") String email
    ) {
        GetBoardListResponseDto response = boardService.getUserBoardList(email);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto> postBoard(
            @RequestBody @Valid PostBoardRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        boardService.postBoard(requestBody, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PostMapping("/{boardNumber}/comment")
    public ResponseEntity<ResponseDto> postComment(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostCommentRequestDto dto
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

    @PatchMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> patchBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchBoardRequestDto requestBody
    ) {
        boardService.patchBoard(requestBody, boardNumber, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PatchMapping("{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> patchComment(
            @AuthenticationPrincipal String email,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber,
            @RequestBody @Valid PatchCommentRequestDto dto
    ) {
        boardService.patchComment(boardNumber, commentNumber, email, dto);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @DeleteMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> deleteBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ) {
        boardService.deleteBoard(boardNumber, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @DeleteMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> deleteComment(
            @AuthenticationPrincipal String email,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber
    ) {
        boardService.deleteComment(boardNumber, email, commentNumber);
        return ResponseEntity.ok(ResponseDto.success());
    }
}
