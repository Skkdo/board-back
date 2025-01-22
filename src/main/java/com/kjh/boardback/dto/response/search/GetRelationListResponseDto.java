package com.kjh.boardback.dto.response.search;

import java.util.List;
import lombok.Getter;

@Getter
public class GetRelationListResponseDto {

    private List<String> relativeWordList;

    public GetRelationListResponseDto(List<String> relationList) {
        this.relativeWordList = relationList;
    }
}
