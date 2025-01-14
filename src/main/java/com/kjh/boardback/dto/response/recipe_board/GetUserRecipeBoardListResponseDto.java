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
public class GetUserRecipeBoardListResponseDto extends ResponseDto {

    private List<RecipeBoardListItem> userBoardList;

    private GetUserRecipeBoardListResponseDto(List<RecipeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        List<RecipeBoardListItem> boardListItems = RecipeBoardListItem.getList(boardListViewEntities);
        this.userBoardList = boardListItems;
    }

    public static ResponseEntity<GetUserRecipeBoardListResponseDto> success(List<RecipeBoardListViewEntity> boardListViewEntities){
        GetUserRecipeBoardListResponseDto result = new GetUserRecipeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
