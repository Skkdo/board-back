package com.kjh.boardback.domain.trade_board.dto.response;

import com.kjh.boardback.domain.trade_board.dto.object.TradeCommentListItem;
import com.kjh.boardback.domain.trade_board.entity.TradeComment;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeCommentListResponseDto {

    private List<TradeCommentListItem> commentList;

    public GetTradeCommentListResponseDto(List<TradeComment> commentList) {
        this.commentList = TradeCommentListItem.copyList(commentList);
    }

}
