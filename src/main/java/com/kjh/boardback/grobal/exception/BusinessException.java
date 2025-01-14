package com.kjh.boardback.grobal.exception;

import com.kjh.boardback.grobal.common.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private ResponseCode responseCode;

    public BusinessException(ResponseCode responseCode){
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
