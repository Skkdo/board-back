package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.board.PatchCommentRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PatchRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeBoardRequestDto;
import com.kjh.boardback.dto.request.recipe_board.PostRecipeCommentRequestDto;
import com.kjh.boardback.dto.response.recipe_board.GetLatestRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeBoardResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeCommentListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetSearchRecipeBoardListResponseDto;
import com.kjh.boardback.dto.response.recipe_board.GetUserRecipeBoardListResponseDto;
import com.kjh.boardback.global.common.ResponseCode;
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
    public ResponseEntity<GetRecipeBoardResponseDto> getBoard(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetRecipeBoardResponseDto response = boardService.getBoard(boardNumber);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<GetRecipeFavoriteListResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetRecipeFavoriteListResponseDto response = boardService.getFavoriteList(boardNumber);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<GetRecipeCommentListResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetRecipeCommentListResponseDto response = boardService.getCommentList(boardNumber);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<ResponseDto> increaseViewCount(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.increaseViewCount(boardNumber);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @GetMapping("/latest-list/{type}")
    public ResponseEntity<GetLatestRecipeBoardListResponseDto> getLatestBoardList(
            @PathVariable("type") Integer type
    ) {
        GetLatestRecipeBoardListResponseDto response = boardService.getLatestBoardList(type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{type}/top-3")
    public ResponseEntity<? extends ResponseDto> getTop3BoardList(
            @PathVariable("type") Integer type
    ) {
        ResponseDto response = boardService.getTop3BoardList(type);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"/search-list/{searchWord}", "/search-list/{searchWord}/{preSearchWord}"})
    public ResponseEntity<GetSearchRecipeBoardListResponseDto> getSearchBoardList(
            @PathVariable("searchWord") String searchWord,
            @PathVariable(value = "preSearchWord", required = false) String PreSearchWord
    ) {
        GetSearchRecipeBoardListResponseDto response = boardService.getSearchBoardList(searchWord,
                PreSearchWord);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-board-list/{email}")
    public ResponseEntity<GetUserRecipeBoardListResponseDto> getUserBoardList(
            @PathVariable("email") String email
    ) {
        GetUserRecipeBoardListResponseDto response = boardService.getUserBoardList(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDto> postBoard(
            @RequestBody @Valid PostRecipeBoardRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        boardService.postBoard(requestBody, email);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @PostMapping("/{boardNumber}/comment")
    public ResponseEntity<ResponseDto> postComment(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostRecipeCommentRequestDto dto
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
            @RequestBody @Valid PatchRecipeBoardRequestDto requestBody
    ) {
        boardService.patchBoard(requestBody, boardNumber, email);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @PatchMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> patchComment(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchCommentRequestDto dto,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber
    ) {
        boardService.patchComment(boardNumber, commentNumber, email, dto);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @DeleteMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> deleteBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email
    ) {
        boardService.deleteBoard(boardNumber, email);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }

    @DeleteMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> deleteComment(
            @AuthenticationPrincipal String email,
            @PathVariable("boardNumber") Integer boardNumber,
            @PathVariable("commentNumber") Integer commentNumber
    ) {
        boardService.deleteComment(boardNumber, commentNumber, email);
        return ResponseEntity.ok().body(new ResponseDto(ResponseCode.SUCCESS));
    }
}
