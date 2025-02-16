package com.kjh.boardback.domain.search_log.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class GetRelationListResponseDto {

    private List<String> relativeWordList;

    public GetRelationListResponseDto(List<String> relationList) {
        this.relativeWordList = relationList;
    }
}
