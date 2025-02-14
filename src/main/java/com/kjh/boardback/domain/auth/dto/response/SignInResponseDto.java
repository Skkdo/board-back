package com.kjh.boardback.domain.auth.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInResponseDto {

    private String token;

    @Value("${jwt.expiration.time}")
    private int expirationTime;

    public SignInResponseDto(String token) {
        this.token = token;
    }
}
