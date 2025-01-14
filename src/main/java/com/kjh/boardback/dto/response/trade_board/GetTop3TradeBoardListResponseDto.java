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
public class GetTop3TradeBoardListResponseDto extends ResponseDto {

    private List<TradeBoardListItem> tradetop3List;
    private GetTop3TradeBoardListResponseDto(List<TradeBoardListViewEntity> boardListViewEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.tradetop3List = TradeBoardListItem.getList(boardListViewEntities);

    }

    public static ResponseEntity<GetTop3TradeBoardListResponseDto> success(List<TradeBoardListViewEntity> boardListViewEntities){
        GetTop3TradeBoardListResponseDto result = new GetTop3TradeBoardListResponseDto(boardListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
