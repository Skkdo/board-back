package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.dto.object.TradeCommentListItem;
import com.kjh.boardback.entity.trade_board.TradeComment;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeCommentListResponseDto {

    private List<TradeCommentListItem> commentList;

    public GetTradeCommentListResponseDto(List<TradeComment> commentList) {
        this.commentList = TradeCommentListItem.copyList(commentList);
    }

}
