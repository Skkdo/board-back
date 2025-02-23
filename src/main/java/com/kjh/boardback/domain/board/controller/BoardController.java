package com.kjh.boardback.domain.board.controller;

import com.kjh.boardback.domain.board.dto.request.PatchBoardRequestDto;
import com.kjh.boardback.domain.board.dto.request.PostBoardRequestDto;
import com.kjh.boardback.domain.board.dto.response.GetBoardListResponseDto;
import com.kjh.boardback.domain.board.dto.response.GetBoardPageListResponseDto;
import com.kjh.boardback.domain.board.dto.response.GetBoardResponseDto;
import com.kjh.boardback.domain.board.service.BoardService;
import com.kjh.boardback.global.common.ResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/{boardNumber}/increase-view-count")
    public ResponseEntity<ResponseDto> increaseViewCount(
            @PathVariable("boardNumber") Integer boardNumber
    ) {
        boardService.increaseViewCount(boardNumber);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @GetMapping("/latest-list")
    public ResponseEntity<ResponseDto> getLatestBoardList(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable
    ) {
        GetBoardPageListResponseDto response = boardService.getLatestBoardList(pageable);
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

    @PatchMapping("/{boardNumber}")
    public ResponseEntity<ResponseDto> patchBoard(
            @PathVariable("boardNumber") Integer boardNumber,
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PatchBoardRequestDto requestBody
    ) {
        boardService.patchBoard(requestBody, boardNumber, email);
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

}
