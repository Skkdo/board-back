package com.kjh.boardback.domain.search_log.controller;

import com.kjh.boardback.domain.search_log.dto.response.GetPopularListResponseDto;
import com.kjh.boardback.domain.search_log.dto.response.GetRelationListResponseDto;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.domain.search_log.service.SearchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
public class SearchLogController {

    private final SearchLogService searchLogService;

    @GetMapping("/popular-list")
    public ResponseEntity<ResponseDto> getPopularList() {
        GetPopularListResponseDto response = searchLogService.getPopularList();
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/{searchWord}/relation-list")
    public ResponseEntity<ResponseDto> getRelationList(
            @PathVariable("searchWord") String searchWord
    ) {
        GetRelationListResponseDto response = searchLogService.getRelationList(searchWord);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

}
