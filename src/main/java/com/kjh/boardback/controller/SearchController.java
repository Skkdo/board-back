package com.kjh.boardback.controller;

import com.kjh.boardback.dto.response.search.GetPopularListResponseDto;
import com.kjh.boardback.dto.response.search.GetRelationListResponseDto;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/popular-list")
    public ResponseEntity<ResponseDto> getPopularList() {
        GetPopularListResponseDto response = searchService.getPopularList();
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/{searchWord}/relation-list")
    public ResponseEntity<ResponseDto> getRelationList(
            @PathVariable("searchWord") String searchWord
    ) {
        GetRelationListResponseDto response = searchService.getRelationList(searchWord);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

}
