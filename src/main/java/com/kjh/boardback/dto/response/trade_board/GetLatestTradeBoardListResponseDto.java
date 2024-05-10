package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.common.ResponseCode;
import com.kjh.boardback.common.ResponseMessage;
import com.kjh.boardback.dto.object.TradeBoardListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetLatestTradeBoardListResponseDto extends ResponseDto {

    private List<TradeBoardListItem> tradeBoardListItemList;

    private GetLatestTradeBoardListResponseDto(List<TradeBoardListItem> tradeBoardListItems) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        tradeBoardListItemList = tradeBoardListItems;
    }

    public static ResponseEntity<GetLatestTradeBoardListResponseDto> success(List<TradeBoardListItem> tradeBoardListItems) {
        GetLatestTradeBoardListResponseDto result = new GetLatestTradeBoardListResponseDto(tradeBoardListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
