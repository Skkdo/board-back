package com.kjh.boardback.dto.response.search;

import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRelationListResponseDto extends ResponseDto {

    private List<String> relativeWordList;

    public GetRelationListResponseDto(List<String> relationList) {
        super(ResponseCode.SUCCESS);
        this.relativeWordList = relationList;
    }
}
