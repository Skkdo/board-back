package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetTop3RecipeBoardListResponseDto extends ResponseDto {
    private GetTop3RecipeBoardListResponseDto(String code, String message) {
        super(code, message);
    }

    public static ResponseEntity<? extends ResponseDto> getTop3DatabaseError(){
        GetTop3RecipeBoardListResponseDto responseBody = new GetTop3RecipeBoardListResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }
    public static ResponseEntity<? extends ResponseDto> getTop3TypeError(){
        GetTop3RecipeBoardListResponseDto responseBody = new GetTop3RecipeBoardListResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
