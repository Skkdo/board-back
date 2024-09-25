package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.account_log.Datetime;
import com.kjh.boardback.dto.request.account_log.PatchAccountLogRequestDto;
import com.kjh.boardback.dto.request.account_log.PostAccountLogRequestDto;
import com.kjh.boardback.dto.request.account_log.PostMoneyCustomTypeRequestDto;
import com.kjh.boardback.dto.response.account_log.*;
import com.kjh.boardback.service.AccountLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account-log")
public class AccountLogController {
    private final AccountLogService accountLogService;

    @PatchMapping("/{accountLogNumber}")
    ResponseEntity<? super PatchAccountLogResponseDto> patchAccountLog(
            @AuthenticationPrincipal String email,
            @PathVariable("accountLogNumber") Integer accountLogNumber,
            @RequestBody @Valid PatchAccountLogRequestDto dto
    ) {
        ResponseEntity<? super PatchAccountLogResponseDto> response = accountLogService.patchAccountLog(email, accountLogNumber, dto);
        return response;
    }

    @PostMapping("")
    ResponseEntity<? super PostAccountLogResponseDto> postAccountLog(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostAccountLogRequestDto dto
    ) {
        ResponseEntity<? super PostAccountLogResponseDto> response = accountLogService.postAccountLog(email, dto);
        return response;
    }

    @PostMapping("/custom-type")
    ResponseEntity<? super PostMoneyCustomTypeResponseDto> postMoneyCustomType(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid PostMoneyCustomTypeRequestDto dto
    ) {
        ResponseEntity<? super PostMoneyCustomTypeResponseDto> response = accountLogService.postMoneyCustomType(email, dto);
        return response;
    }

    @DeleteMapping("/{accountLogNumber}")
    ResponseEntity<? super DeleteAccountLogResponseDto> deleteAccountLog(
            @AuthenticationPrincipal String email,
            @PathVariable("accountLogNumber") Integer accountLogNumber
    ) {
        ResponseEntity<? super DeleteAccountLogResponseDto> response = accountLogService.deleteAccountLog(email, accountLogNumber);
        return response;
    }

    @DeleteMapping("/custom-type/{customTypeNumber}")
    ResponseEntity<? super DeleteMonetCustomTypeResponseDto> deleteCustomType(
            @AuthenticationPrincipal String email,
            @PathVariable("customTypeNumber") Integer customTypeNumber
    ) {
        ResponseEntity<? super DeleteMonetCustomTypeResponseDto> response = accountLogService.deleteMoneyCustomType(email, customTypeNumber);
        return response;
    }

    @GetMapping("/custom-type")
    ResponseEntity<? super GetMoneyCustomTypeResponseDto> getCustomType(
            @AuthenticationPrincipal String email
    ) {
        ResponseEntity<? super GetMoneyCustomTypeResponseDto> response = accountLogService.getMoneyCustomType(email);
        return response;
    }

    @GetMapping("/day")
    ResponseEntity<? super GetDayAccountLogResponseDto> getDayAccountLog(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid Datetime requestBody
    ) {
        String datetime = requestBody.getDatetime();
        ResponseEntity<? super GetDayAccountLogResponseDto> response = accountLogService.getDayAccountLog(email, datetime);
        return response;
    }

    @GetMapping("/calender")
    ResponseEntity<? super GetCalenderResponseDto> getCalender(
            @AuthenticationPrincipal String email,
            @RequestBody @Valid Datetime requestBody
    ) {
        String datetime = requestBody.getDatetime();
        ResponseEntity<? super GetCalenderResponseDto> response = accountLogService.getCalender(email, datetime);
        return response;
    }
}
