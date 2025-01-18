package com.kjh.boardback.global.exception;

import com.kjh.boardback.global.common.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private ResponseCode responseCode;

    public BusinessException(ResponseCode responseCode){
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
