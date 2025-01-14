package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseMessage;
import com.kjh.boardback.dto.object.RecipeBoardListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.board.BoardListViewEntity;
import com.kjh.boardback.entity.recipe_board.RecipeBoardListViewEntity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetLatestRecipeBoardListResponseDto extends ResponseDto {

    private final List<RecipeBoardListItem> recipelatestList;

    private GetLatestRecipeBoardListResponseDto(List<RecipeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.recipelatestList = RecipeBoardListItem.getList(boardListViewEntities);


    }

    public static ResponseEntity<GetLatestRecipeBoardListResponseDto> success(List<RecipeBoardListViewEntity> boardListViewEntities) {
        GetLatestRecipeBoardListResponseDto result = new GetLatestRecipeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
