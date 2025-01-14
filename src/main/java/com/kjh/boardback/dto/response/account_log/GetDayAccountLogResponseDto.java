package com.kjh.boardback.dto.response.account_log;

import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseMessage;
import com.kjh.boardback.dto.object.AccountLogItem;
import com.kjh.boardback.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetDayAccountLogResponseDto extends ResponseDto {
    List<AccountLogItem> accountLogItemList;
    int totalIncome;
    int totalExpense;
    private GetDayAccountLogResponseDto(List<AccountLogItem> accountLogItemList) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.accountLogItemList = accountLogItemList;
        int income =0;
        int expense =0;
        for(AccountLogItem accountLogItem : accountLogItemList){
            int type = accountLogItem.getType();
            int money = accountLogItem.getMoney();
            if(type == 0){
                income += money;
            }
            if(type == 1){
                expense -= money;
            }
        }
        this.totalIncome =income;
        this.totalExpense = expense;
    }
    public static ResponseEntity<GetDayAccountLogResponseDto> success(List<AccountLogItem> accountLogItemList){
        GetDayAccountLogResponseDto result = new GetDayAccountLogResponseDto(accountLogItemList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
