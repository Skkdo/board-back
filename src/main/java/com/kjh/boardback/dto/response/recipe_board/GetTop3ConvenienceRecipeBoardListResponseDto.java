package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.object.RecipeBoardListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.recipe_board.RecipeBoardListViewEntity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetTop3ConvenienceRecipeBoardListResponseDto extends ResponseDto {

    private List<RecipeBoardListItem> conveniencerecipetop3List;
    private GetTop3ConvenienceRecipeBoardListResponseDto(List<RecipeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.conveniencerecipetop3List = RecipeBoardListItem.getList(boardListViewEntities);

    }

    public static ResponseEntity<GetTop3ConvenienceRecipeBoardListResponseDto> success(List<RecipeBoardListViewEntity> boardListViewEntities){
        GetTop3ConvenienceRecipeBoardListResponseDto result = new GetTop3ConvenienceRecipeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
