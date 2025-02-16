package com.kjh.boardback.domain.search_log.service;


import com.kjh.boardback.domain.search_log.dto.response.GetPopularListResponseDto;
import com.kjh.boardback.domain.search_log.dto.response.GetRelationListResponseDto;
import com.kjh.boardback.domain.search_log.entity.SearchLog;
import com.kjh.boardback.domain.search_log.repository.SearchLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchLogService {

    private final SearchLogRepository searchLogRepository;

    public GetRelationListResponseDto getRelationList(String searchWord) {

        List<String> relationList = searchLogRepository.getRelationList(searchWord);
        return new GetRelationListResponseDto(relationList);
    }

    public GetPopularListResponseDto getPopularList() {

        List<SearchLog> searchLogList = searchLogRepository.getPopularList();
        return new GetPopularListResponseDto(searchLogList);
    }

    public SearchLog save(SearchLog searchLog) {
        return searchLogRepository.save(searchLog);
    }
}
