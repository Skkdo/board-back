package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.dto.object.TradeBoardListItem;
import com.kjh.boardback.entity.trade_board.TradeBoard;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeBoardListResponseDto {
    private final List<TradeBoardListItem> boardList;

    public GetTradeBoardListResponseDto(List<TradeBoard> boardList) {
        this.boardList = TradeBoardListItem.getList(boardList);
    }
}
