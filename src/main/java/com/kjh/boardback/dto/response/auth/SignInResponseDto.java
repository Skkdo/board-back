package com.kjh.boardback.dto.response.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@NoArgsConstructor
public class SignInResponseDto {

    private String token;

    @Value("${jwt.expiration.time}")
    private int expirationTime;

    public SignInResponseDto(String token) {
        this.token = token;
    }
}
