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
public class GetLatestBoardListResponseDto extends ResponseDto {

    private final List<BoardListItem> latestList;

    private GetLatestBoardListResponseDto(List<BoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.latestList = BoardListItem.getList(boardListViewEntities);


    }

    public static ResponseEntity<GetLatestBoardListResponseDto> success(List<BoardListViewEntity> boardListViewEntities) {
        GetLatestBoardListResponseDto result = new GetLatestBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
