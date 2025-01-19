package com.kjh.boardback.dto.response.search;

import com.kjh.boardback.entity.SearchLog;
import java.util.List;
import lombok.Getter;

@Getter
public class GetPopularListResponseDto {

    private List<String> popularWordList;

    public GetPopularListResponseDto(List<SearchLog> searchLogList) {
        this.popularWordList = searchLogList.stream().map(SearchLog::getSearchWord).toList();
    }
}
