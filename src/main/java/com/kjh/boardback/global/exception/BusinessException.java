package com.kjh.boardback.global.exception;

import com.kjh.boardback.global.common.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final int status;
    private final String code;
    private final String message;

    public BusinessException(ResponseCode responseCode){
        super(responseCode.getMessage());
        this.status = responseCode.getStatus();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();

    }
}
