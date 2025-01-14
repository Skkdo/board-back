package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseMessage;
import com.kjh.boardback.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class IncreaseRecipeViewCountResponseDto extends ResponseDto {
    private IncreaseRecipeViewCountResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<IncreaseRecipeViewCountResponseDto> success(){
        IncreaseRecipeViewCountResponseDto result = new IncreaseRecipeViewCountResponseDto();
        return ResponseEntity.status(HttpStatus.OK ).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistBoard(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_BOARD, ResponseMessage.NOT_EXISTED_BOARD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST ).body(result);
    }
}
