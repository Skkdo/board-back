package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.board.PatchBoardRequestDto;
import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.board.PostBoardRequestDto;
import com.kjh.boardback.dto.request.board.PostCommentRequestDto;
import com.kjh.boardback.dto.response.board.*;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/community/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{boardNumber}")
    public ResponseEntity<GetBoardResponseDto> getBoard(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetBoardResponseDto response = boardService.getBoard(boardNumber);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<GetFavoriteListResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetFavoriteListResponseDto response = boardService.getFavoriteList(boardNumber);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<GetCommentListResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetCommentListResponseDto response = boardService.getCommentList(boardNumber);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<ResponseDto> increaseViewCount(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.increaseViewCount(boardNumber);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @GetMapping("/latest-list")
    public ResponseEntity<GetLatestBoardListResponseDto> getLatestBoardList(){
        GetLatestBoardListResponseDto response = boardService.getLatestBoardList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/top-3")
    public ResponseEntity<GetTop3BoardListResponseDto> getTop3BoardList(){
        GetTop3BoardListResponseDto response = boardService.getTop3BoardList();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = {"/search-list/{searchWord}","/search-list/{searchWord}/{preSearchWord}"})
    public ResponseEntity<GetSearchBoardListResponseDto> getSearchBoardList(
            @PathVariable("searchWord") String searchWord,
            @PathVariable(value = "preSearchWord",required = false) String PreSearchWord
    ){
        GetSearchBoardListResponseDto response = boardService.getSearchBoardList(searchWord, PreSearchWord);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-board-list/{email}")
    public ResponseEntity<GetUserBoardListResponseDto> getUserBoardList(
            @PathVariable("email") String email
    ){
        GetUserBoardListResponseDto response = boardService.getUserBoardList(email);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto> postBoard(
            @RequestBody @Valid PostBoardRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        boardService.postBoard(requestBody, email);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @PostMapping("/{boardNumber}/comment")
    public ResponseEntity<ResponseDto> postComment(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostCommentRequestDto dto
    ) {
        boardService.postComment(boardNumber, email, dto);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }


    @PutMapping("/{boardNumber}/favorite")
    public ResponseEntity<ResponseDto> putFavorite(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ) {
        boardService.putFavorite(email, boardNumber);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @PatchMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> patchBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchBoardRequestDto requestBody
            ){
        boardService.patchBoard(requestBody, boardNumber, email);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @PatchMapping("{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> patchComment(
            @AuthenticationPrincipal String email,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber,
            @RequestBody @Valid PatchCommentRequestDto dto
            ){
        boardService.patchComment(boardNumber, commentNumber,email, dto);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @DeleteMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> deleteBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ){
        boardService.deleteBoard(boardNumber, email);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @DeleteMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> deleteComment(
        @AuthenticationPrincipal String email,
        @PathVariable("boardNumber") Integer boardNumber,
        @PathVariable("commentNumber") Integer commentNumber
    ){
        boardService.deleteComment(boardNumber,email,commentNumber);
        return ResponseEntity.ok(new ResponseDto(ResponseCode.SUCCESS));
    }
}
