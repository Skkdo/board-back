package com.kjh.boardback.domain.trade_board.controller;

import com.kjh.boardback.domain.trade_board.dto.request.PatchTradeCommentRequestDto;
import com.kjh.boardback.domain.trade_board.dto.request.PostTradeCommentRequestDto;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeCommentListResponseDto;
import com.kjh.boardback.domain.trade_board.service.TradeCommentService;
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
public class TradeCommentController {

    private final TradeCommentService boardService;

    @GetMapping("/{boardNumber}/comment-list")
    public ResponseEntity<ResponseDto> getCommentList(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        GetTradeCommentListResponseDto response = boardService.getCommentList(boardNumber);
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
