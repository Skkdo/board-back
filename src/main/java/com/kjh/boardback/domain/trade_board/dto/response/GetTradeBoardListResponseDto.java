package com.kjh.boardback.domain.trade_board.dto.response;

import com.kjh.boardback.domain.trade_board.dto.object.TradeBoardListItem;
import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeBoardListResponseDto {
    private final List<TradeBoardListItem> boardList;

    public GetTradeBoardListResponseDto(List<TradeBoard> boardList) {
        this.boardList = TradeBoardListItem.getList(boardList);
    }
}
