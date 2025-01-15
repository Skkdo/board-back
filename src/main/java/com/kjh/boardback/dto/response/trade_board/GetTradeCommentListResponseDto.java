package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.dto.object.TradeCommentListItem;
import com.kjh.boardback.entity.trade_board.TradeComment;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeCommentListResponseDto extends ResponseDto {

    private List<TradeCommentListItem> commentList;

    public GetTradeCommentListResponseDto(List<TradeComment> commentList) {
        super(ResponseCode.SUCCESS);
        this.commentList = TradeCommentListItem.copyList(commentList);
    }

}
