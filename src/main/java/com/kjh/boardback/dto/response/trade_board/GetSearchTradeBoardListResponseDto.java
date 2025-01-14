package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.object.TradeBoardListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.trade_board.TradeBoardListViewEntity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetSearchTradeBoardListResponseDto extends ResponseDto {

    private List<TradeBoardListItem> searchList;

    private GetSearchTradeBoardListResponseDto(List<TradeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.searchList = TradeBoardListItem.getList(boardListViewEntities);
    }

    public static ResponseEntity<GetSearchTradeBoardListResponseDto> success(List<TradeBoardListViewEntity> boardListViewEntities){
        GetSearchTradeBoardListResponseDto result = new GetSearchTradeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
