package com.kjh.boardback.service;

import com.kjh.boardback.dto.request.account_log.PatchAccountLogRequestDto;
import com.kjh.boardback.dto.request.account_log.PostAccountLogRequestDto;
import com.kjh.boardback.dto.request.account_log.PostMoneyCustomTypeRequestDto;
import com.kjh.boardback.dto.response.account_log.*;
import org.springframework.http.ResponseEntity;

public interface AccountLogService {
    ResponseEntity<? super DeleteAccountLogResponseDto> deleteAccountLog(String email,int accountLogNumber);
    ResponseEntity<? super DeleteMonetCustomTypeResponseDto> deleteMoneyCustomType(String email, int customTypeNumber);
    ResponseEntity<? super PatchAccountLogResponseDto> patchAccountLog(String email, int accountLogNumber,PatchAccountLogRequestDto dto);
    ResponseEntity<? super PostMoneyCustomTypeResponseDto> postMoneyCustomType(String email, PostMoneyCustomTypeRequestDto dto);
    ResponseEntity<? super PostAccountLogResponseDto> postAccountLog(String email,PostAccountLogRequestDto dto);
    ResponseEntity<? super GetMoneyCustomTypeResponseDto> getMoneyCustomType(String email);
    ResponseEntity<? super GetDayAccountLogResponseDto> getDayAccountLog(String email, String datetime);
    ResponseEntity<? super GetCalenderResponseDto> getCalender(String email,String datetime);
}
