package com.kjh.boardback.domain.recipe_board.controller;

import com.kjh.boardback.domain.recipe_board.dto.request.PatchRecipeCommentRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.request.PostRecipeCommentRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeCommentListResponseDto;
import com.kjh.boardback.domain.recipe_board.service.RecipeCommentService;
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
@RequestMapping("/api/v1/recipe/recipe-board")
public class RecipeBoardCommentController {

    private final RecipeCommentService boardService;

    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<ResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetRecipeCommentListResponseDto response = boardService.getCommentList(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
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

    @PatchMapping("/{boardNumber}/{commentNumber}")
    public ResponseEntity<ResponseDto> patchComment(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchRecipeCommentRequestDto dto,
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
        boardService.deleteComment(boardNumber, commentNumber, email);
        return ResponseEntity.ok().body(ResponseDto.success());
    }
}
