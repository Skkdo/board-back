package com.kjh.boardback.dto.response.search;

import com.kjh.boardback.entity.SearchLog;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPopularListResponseDto {

    private List<String> popularWordList;

    public GetPopularListResponseDto(List<SearchLog> searchLogList) {
        this.popularWordList = searchLogList.stream().map(SearchLog::getSearchWord).toList();
    }
}
