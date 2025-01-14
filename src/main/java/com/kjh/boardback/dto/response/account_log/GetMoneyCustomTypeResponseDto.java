package com.kjh.boardback.dto.response.account_log;

import com.kjh.boardback.grobal.common.ResponseCode;
import com.kjh.boardback.grobal.common.ResponseMessage;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.entity.account_log.MoneyCustomTypeEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@Getter
@Setter
public class GetMoneyCustomTypeResponseDto extends ResponseDto {
    private List<MoneyCustomTypeEntity> moneyCustomTypeEntityList;
    private GetMoneyCustomTypeResponseDto(List<MoneyCustomTypeEntity> moneyCustomTypeEntityList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.moneyCustomTypeEntityList = moneyCustomTypeEntityList;
    }
    public static ResponseEntity<GetMoneyCustomTypeResponseDto> success(List<MoneyCustomTypeEntity> moneyCustomTypeEntityList){
        GetMoneyCustomTypeResponseDto result = new GetMoneyCustomTypeResponseDto(moneyCustomTypeEntityList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
