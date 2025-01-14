package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class IncreaseTradeViewCountResponseDto extends ResponseDto {
    private IncreaseTradeViewCountResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<IncreaseTradeViewCountResponseDto> success(){
        IncreaseTradeViewCountResponseDto result = new IncreaseTradeViewCountResponseDto();
        return ResponseEntity.status(HttpStatus.OK ).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(result);
    }
}
