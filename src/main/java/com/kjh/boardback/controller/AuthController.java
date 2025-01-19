package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.auth.SignInRequestDto;
import com.kjh.boardback.dto.request.auth.SignUpRequestDto;
import com.kjh.boardback.dto.response.auth.SignInResponseDto;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto> signUp(
            @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        authService.signUp(requestBody);
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto> signIn(
            @RequestBody @Valid SignInRequestDto requestBody
    ) {
        SignInResponseDto response = authService.signIn(requestBody);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }


}
