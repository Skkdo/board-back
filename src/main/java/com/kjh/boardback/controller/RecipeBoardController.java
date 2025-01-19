package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeCommentRequestDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeCommentListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.service.RecipeBoardService;
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
@RequestMapping("/api/v1/recipe/recipe-board")
public class RecipeBoardController {

    private final RecipeBoardService boardService;

    @GetMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> getBoard(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetRecipeBoardResponseDto response = boardService.getBoard(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<ResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetRecipeFavoriteListResponseDto response = boardService.getFavoriteList(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }


    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<ResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetRecipeCommentListResponseDto response = boardService.getCommentList(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @GetMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<ResponseDto> increaseViewCount(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.increaseViewCount(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @GetMapping("/latest-list/{type}")
    public ResponseEntity<ResponseDto> getLatestBoardList(
            @PathVariable("type") Integer type
    ) {
        GetRecipeBoardListResponseDto response = boardService.getLatestBoardList(type);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/{type}/top-3")
    public ResponseEntity<ResponseDto> getTop3BoardList(
            @PathVariable("type") Integer type
    ) {
        GetRecipeBoardListResponseDto response = boardService.getTop3BoardList(type);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping(value = {"/search-list/{searchWord}", "/search-list/{searchWord}/{preSearchWord}"})
    public ResponseEntity<ResponseDto> getSearchBoardList(
            @PathVariable("searchWord") String searchWord,
            @PathVariable(value = "preSearchWord", required = false) String PreSearchWord
    ) {
        GetRecipeBoardListResponseDto response = boardService.getSearchBoardList(searchWord, PreSearchWord);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/user-board-list/{email}")
    public ResponseEntity<ResponseDto> getUserBoardList(
            @PathVariable("email") String email
    ) {
        GetRecipeBoardListResponseDto response = boardService.getUserBoardList(email);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto> postBoard(
            @RequestBody @Valid PostRecipeBoardRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        boardService.postBoard(requestBody, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PostMapping("/{boardNumber}/comment")
    public ResponseEntity<ResponseDto> postComment(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostRecipeCommentRequestDto dto
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
            @RequestBody @Valid PatchRecipeBoardRequestDto requestBody
    ) {
        boardService.patchBoard(requestBody, boardNumber, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PatchMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> patchComment(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchCommentRequestDto dto,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber
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
        boardService.deleteComment(boardNumber, commentNumber, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }
}
