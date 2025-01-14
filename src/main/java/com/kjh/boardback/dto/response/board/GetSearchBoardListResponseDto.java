package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.object.BoardListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.board.BoardListViewEntity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetSearchBoardListResponseDto extends ResponseDto {

    private List<BoardListItem> searchList;

    private GetSearchBoardListResponseDto(List<BoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.searchList = BoardListItem.getList(boardListViewEntities);
    }

    public static ResponseEntity<GetSearchBoardListResponseDto> success(List<BoardListViewEntity> boardListViewEntities){
        GetSearchBoardListResponseDto result = new GetSearchBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
