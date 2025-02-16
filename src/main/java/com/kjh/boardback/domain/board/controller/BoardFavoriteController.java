package com.kjh.boardback.domain.board.controller;

import com.kjh.boardback.domain.board.dto.response.GetFavoriteListResponseDto;
import com.kjh.boardback.domain.board.service.BoardFavoriteService;
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
@RequestMapping("/api/v1/community/board")
public class BoardFavoriteController {

    private final BoardFavoriteService boardService;

    @GetMapping("/{boardNumber}/favorite-list")
    public ResponseEntity<ResponseDto> getFavoriteList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetFavoriteListResponseDto response = boardService.getFavoriteList(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success(response));
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
