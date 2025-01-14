package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseMessage;
import com.kjh.boardback.dto.object.TradeBoardListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.trade_board.TradeBoardListViewEntity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetUserTradeBoardListResponseDto extends ResponseDto {

    private List<TradeBoardListItem> userBoardList;

    private GetUserTradeBoardListResponseDto(List<TradeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        List<TradeBoardListItem> boardListItems = TradeBoardListItem.getList(boardListViewEntities);
        this.userBoardList = boardListItems;
    }

    public static ResponseEntity<GetUserTradeBoardListResponseDto> success(List<TradeBoardListViewEntity> boardListViewEntities){
        GetUserTradeBoardListResponseDto result = new GetUserTradeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
