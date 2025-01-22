package com.kjh.boardback.global.common;

import com.kjh.boardback.global.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private String code;
    private String message;
    private Object data;

    private ResponseDto(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = null;
    }

    private ResponseDto(ResponseCode responseCode, Object data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    private ResponseDto(BusinessException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
        this.data = null;
    }

    public static ResponseDto fail(BusinessException e) {
        return new ResponseDto(e);
    }

    public static ResponseDto fail(ResponseCode responseCode) {
        return new ResponseDto(responseCode);
    }

    public static ResponseDto success() {
        return new ResponseDto(ResponseCode.SUCCESS);
    }

    public static ResponseDto success(Object object) {
        return new ResponseDto(ResponseCode.SUCCESS, object);
    }
}
