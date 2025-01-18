package com.kjh.boardback.service;


import com.kjh.boardback.dto.response.search.GetPopularListResponseDto;
import com.kjh.boardback.dto.response.search.GetRelationListResponseDto;
import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.repository.SearchLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchLogRepository searchLogRepository;

    public GetRelationListResponseDto getRelationList(String searchWord) {

        List<String> relationList = searchLogRepository.getRelationList(searchWord);
        return new GetRelationListResponseDto(relationList);
    }

    public GetPopularListResponseDto getPopularList() {

        List<SearchLog> searchLogList = searchLogRepository.getPopularList();
        return new GetPopularListResponseDto(searchLogList);
    }
}
