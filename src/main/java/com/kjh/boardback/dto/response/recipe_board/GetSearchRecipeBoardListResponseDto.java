package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.object.RecipeBoardListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.board.BoardListViewEntity;
import com.kjh.boardback.entity.recipe_board.RecipeBoardListViewEntity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetSearchRecipeBoardListResponseDto extends ResponseDto {

    private List<RecipeBoardListItem> searchList;

    private GetSearchRecipeBoardListResponseDto(List<RecipeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.searchList = RecipeBoardListItem.getList(boardListViewEntities);
    }

    public static ResponseEntity<GetSearchRecipeBoardListResponseDto> success(List<RecipeBoardListViewEntity> boardListViewEntities){
        GetSearchRecipeBoardListResponseDto result = new GetSearchRecipeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
