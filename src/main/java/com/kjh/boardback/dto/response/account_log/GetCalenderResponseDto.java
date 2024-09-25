package com.kjh.boardback.dto.response.account_log;

import com.kjh.boardback.common.ResponseCode;
import com.kjh.boardback.common.ResponseMessage;
import com.kjh.boardback.dto.object.DayAccountLogItem;
import com.kjh.boardback.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
public class GetCalenderResponseDto extends ResponseDto {
    List<DayAccountLogItem> calender;
    private GetCalenderResponseDto(List<DayAccountLogItem> calender) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.calender =calender;
    }
    public static ResponseEntity<GetCalenderResponseDto> success(List<DayAccountLogItem> calender){
        GetCalenderResponseDto result = new GetCalenderResponseDto(calender);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
}
