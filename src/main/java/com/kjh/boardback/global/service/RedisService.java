package com.kjh.boardback.global.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.domain.board.entity.Board;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> redisStringTemplate;

    private final ObjectMapper objectMapper;

    private final String boardKey = "board:";
    private final String refreshTokenKey = "email:";

    @Value("${refresh.token.expiration.time}")
    private int refreshToken_expirationTime;

    public List<Board> getBoardTop3() {
        Object o = redisTemplate.opsForValue().get(boardKey);
        if (o instanceof List<?> resultList) {
            return resultList.stream()
                    .map(board -> objectMapper.convertValue(board, Board.class))
                    .toList();
        }
        return new ArrayList<>();
    }

    public void setBoardTop3(List<Board> boardList) {
        redisTemplate.opsForValue().set(boardKey, boardList);
    }

    public String getRefreshTokenByEmail(String email) {
        return redisStringTemplate.opsForValue().get(refreshTokenKey + email);
    }

    public void setRefreshToken(String email, String refreshToken) {
        redisStringTemplate.opsForValue().set(refreshTokenKey + email, refreshToken, refreshToken_expirationTime, TimeUnit.SECONDS);
    }
}
