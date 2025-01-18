package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.dto.object.TradeBoardListItem;
import com.kjh.boardback.entity.trade_board.TradeBoard;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetLatestTradeBoardListResponseDto extends ResponseDto {

    private final List<TradeBoardListItem> tradelatestList;

    public GetLatestTradeBoardListResponseDto(List<TradeBoard> latestList) {
        super(ResponseCode.SUCCESS);
        this.tradelatestList = TradeBoardListItem.getList(latestList);
    }
}
