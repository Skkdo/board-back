package com.kjh.boardback.controller;

import com.kjh.boardback.dto.request.user.PatchNicknameRequestDto;
import com.kjh.boardback.dto.request.user.PatchProfileImageRequestDto;
import com.kjh.boardback.dto.response.user.GetSignInUserResponseDto;
import com.kjh.boardback.dto.response.user.GetUserResponseDto;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseDto> getSignInUser(
            @AuthenticationPrincipal String email
    ) {
        GetSignInUserResponseDto response = userService.getSignInUser(email);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ResponseDto> getUser(
            @PathVariable("email") String email
    ) {
        GetUserResponseDto response = userService.getUser(email);
        return ResponseEntity.ok().body(ResponseDto.success(response));
    }

    @PatchMapping("/nickname")
    public ResponseEntity<ResponseDto> patchNickname(
            @RequestBody @Valid PatchNicknameRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        userService.patchNickname(email, requestBody);
        return ResponseEntity.ok(ResponseDto.success());
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<ResponseDto> patchProfileImage(
            @RequestBody @Valid PatchProfileImageRequestDto requestBody,
            @AuthenticationPrincipal String email
    ) {
        userService.patchProfileImage(email, requestBody);
        return ResponseEntity.ok(ResponseDto.success());
    }

}
