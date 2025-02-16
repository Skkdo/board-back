package com.kjh.boardback.domain.auth.controller;

import com.kjh.boardback.domain.auth.dto.request.SignInRequestDto;
import com.kjh.boardback.domain.auth.dto.request.SignUpRequestDto;
import com.kjh.boardback.domain.auth.dto.response.SignInResponseDto;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/refresh")
    public ResponseEntity<ResponseDto> getNewTokenByRefreshToken(
            HttpServletRequest request
    ) {
        SignInResponseDto response = authService.getNewTokenByRefreshToken(request);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }
}
