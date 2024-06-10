package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.board.PatchBoardRequestDto;
import com.kjh.boardback.dto.request.board.PostBoardRequestDto;
import com.kjh.boardback.dto.request.board.PostCommentRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeCommentRequestDto;
import com.kjh.boardback.dto.response.board.*;
import com.kjh.boardback.dto.response.recipe_board.*;
import com.kjh.boardback.service.BoardService;
import com.kjh.boardback.service.RecipeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/recipe/recipe-board")
public class RecipeBoardController {

    private final RecipeBoardService boardService;

    @GetMapping("/{boardNumber}")
    public ResponseEntity<? super GetRecipeBoardResponseDto> getBoard(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        ResponseEntity<? super GetRecipeBoardResponseDto> response = boardService.getBoard(boardNumber);
        return response;
    }

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<? super GetRecipeFavoriteListResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        ResponseEntity<? super GetRecipeFavoriteListResponseDto> response = boardService.getFavoriteList(boardNumber);
        return response;
    }


    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<? super GetRecipeCommentListResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        ResponseEntity<? super GetRecipeCommentListResponseDto> response = boardService.getCommentList(boardNumber);
        return response;
    }

    @GetMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<? super IncreaseRecipeViewCountResponseDto> increaseViewCount(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        ResponseEntity<? super IncreaseRecipeViewCountResponseDto> response = boardService.increaseViewCount(boardNumber);
        return response;
    }

    @GetMapping("/latest-list")
    public ResponseEntity<? super GetLatestRecipeBoardListResponseDto> getLatestBoardList(){
        ResponseEntity<? super GetLatestRecipeBoardListResponseDto> response = boardService.getLatestBoardList();
        return response;
    }

    @GetMapping("/top-3")
    public ResponseEntity<? super GetTop3RecipeBoardListResponseDto> getTop3BoardList(){
        ResponseEntity<? super GetTop3RecipeBoardListResponseDto> response = boardService.getTop3BoardList();
        return response;
    }

    @GetMapping(value = {"/search-list/{searchWord}","/search-list/{searchWord}/{preSearchWord}"})
    public ResponseEntity<? super GetSearchRecipeBoardListResponseDto> getSearchBoardList(
            @PathVariable("searchWord") String searchWord,
            @PathVariable(value = "preSearchWord",required = false) String PreSearchWord
    ){
        ResponseEntity<? super GetSearchRecipeBoardListResponseDto> response = boardService.getSearchBoardList(searchWord, PreSearchWord);
        return response;
    }

    @GetMapping("/user-board-list/{email}")
    public ResponseEntity<? super GetUserRecipeBoardListResponseDto> getUserBoardList(
            @PathVariable("email") String email
    ){
        ResponseEntity<? super GetUserRecipeBoardListResponseDto> response = boardService.getUserBoardList(email);
        return response;
    }

    @PostMapping("")
    public ResponseEntity<? super PostRecipeBoardResponseDto> postBoard(
            @RequestBody @Valid PostRecipeBoardRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        ResponseEntity<? super PostRecipeBoardResponseDto> response = boardService.postBoard(requestBody, email);
        return response;
    }

    @PostMapping("/{boardNumber}/comment")
    public ResponseEntity<? super PostRecipeCommentResponseDto> postComment(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostRecipeCommentRequestDto dto
    ) {
        ResponseEntity<? super PostRecipeCommentResponseDto> response = boardService.postComment(boardNumber, email, dto);
        return response;
    }


    @PutMapping("/{boardNumber}/favorite")
    public ResponseEntity<? super PutRecipeFavoriteResponseDto> putFavorite(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ) {
        ResponseEntity<? super PutRecipeFavoriteResponseDto> response = boardService.putFavorite(email, boardNumber);
        return response;
    }

    @PatchMapping("/{boardNumber}")
    public ResponseEntity<? super PatchRecipeBoardResponseDto> patchBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchRecipeBoardRequestDto requestBody
            ){
        ResponseEntity<? super PatchRecipeBoardResponseDto> response = boardService.patchBoard(requestBody, boardNumber, email);
        return response;
    }

    @DeleteMapping("/{boardNumber}")
    public ResponseEntity<? super DeleteRecipeBoardResponseDto> deleteBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super DeleteRecipeBoardResponseDto> response = boardService.deleteBoard(boardNumber, email);
        return response;
    }


}
