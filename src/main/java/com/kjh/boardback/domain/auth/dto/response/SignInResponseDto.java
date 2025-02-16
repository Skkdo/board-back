package com.kjh.boardback.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInResponseDto {
    private String accessToken;
    private int accessToken_expirationTime;
    private String refreshToken;
    private int refreshToken_expirationTime;
}
