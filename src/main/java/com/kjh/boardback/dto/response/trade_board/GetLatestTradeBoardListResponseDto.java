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
public class GetLatestTradeBoardListResponseDto extends ResponseDto {

    private final List<TradeBoardListItem> tradelatestList;

    private GetLatestTradeBoardListResponseDto(List<TradeBoardListViewEntity> tradeBoardListItems) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.tradelatestList = TradeBoardListItem.getList(tradeBoardListItems);
    }

    public static ResponseEntity<GetLatestTradeBoardListResponseDto> success(List<TradeBoardListViewEntity> tradeBoardListItems) {
        GetLatestTradeBoardListResponseDto result = new GetLatestTradeBoardListResponseDto(tradeBoardListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
