package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseMessage;
import com.kjh.boardback.dto.object.TradeCommentListItem;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.repository.resultSet.GetTradeCommentListResultSet;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetTradeCommentListResponseDto extends ResponseDto {

    private List<TradeCommentListItem> commentList;

    private GetTradeCommentListResponseDto(List<GetTradeCommentListResultSet> resultSets) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.commentList = TradeCommentListItem.copyList(resultSets);
    }

    public static ResponseEntity<GetTradeCommentListResponseDto> success(List<GetTradeCommentListResultSet> resultSets){
        GetTradeCommentListResponseDto result = new GetTradeCommentListResponseDto(resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard(){
        ResponseDto result  = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
