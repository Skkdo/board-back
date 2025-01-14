package com.kjh.boardback.grobal.exception;

import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.grobal.common.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BadRequestExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ResponseDto> validationExceptionHandler(Exception e) {
        log.warn(e.getMessage(), e);
        ResponseDto responseDto = new ResponseDto(ResponseCode.VALIDATION_FAILED);
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseDto> BusinessExceptionHandler(BusinessException e) {
        log.warn(e.getMessage(), e);
        ResponseDto responseDto = new ResponseDto(e.getResponseCode());
        return ResponseEntity.status(responseDto.getStatus()).body(responseDto);
    }

}
