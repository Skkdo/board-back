package com.kjh.boardback.dto.response;

import com.kjh.boardback.grobal.common.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {

    private int status;
    private String code;
    private String message;

    public ResponseDto(ResponseCode responseCode) {
        this.status = responseCode.getStatus();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }
}
