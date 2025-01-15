package com.kjh.boardback.dto.response.search;

import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetPopularListResponseDto extends ResponseDto {

    private List<String> popularWordList;

    public GetPopularListResponseDto(List<SearchLog> searchLogList) {
        super(ResponseCode.SUCCESS);
        this.popularWordList = searchLogList.stream().map(SearchLog::getSearchWord).toList();
    }
}
