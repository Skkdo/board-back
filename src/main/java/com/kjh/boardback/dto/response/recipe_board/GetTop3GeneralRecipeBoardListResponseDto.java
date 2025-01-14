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
public class GetTop3GeneralRecipeBoardListResponseDto extends ResponseDto {

    private List<RecipeBoardListItem> generalrecipetop3List;
    private GetTop3GeneralRecipeBoardListResponseDto(List<RecipeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.generalrecipetop3List = RecipeBoardListItem.getList(boardListViewEntities);

    }

    public static ResponseEntity<GetTop3GeneralRecipeBoardListResponseDto> success(List<RecipeBoardListViewEntity> boardListViewEntities){
        GetTop3GeneralRecipeBoardListResponseDto result = new GetTop3GeneralRecipeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
