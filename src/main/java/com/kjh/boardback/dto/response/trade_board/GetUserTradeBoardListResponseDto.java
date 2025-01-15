package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.dto.object.TradeBoardListItem;
import com.kjh.boardback.entity.trade_board.TradeBoard;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetUserTradeBoardListResponseDto extends ResponseDto {

    private List<TradeBoardListItem> userBoardList;

    public GetUserTradeBoardListResponseDto(List<TradeBoard> boardList) {
        super(ResponseCode.SUCCESS);
        this.userBoardList = TradeBoardListItem.getList(boardList);
    }
}
