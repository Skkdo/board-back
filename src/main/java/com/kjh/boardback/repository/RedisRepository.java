package com.kjh.boardback.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.entity.board.Board;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private final String boardKey = "board";

    public List<Board> getBoardTop3Values() {
        Object o = redisTemplate.opsForValue().get(boardKey);
        if (o instanceof List<?> resultList) {
            return resultList.stream()
                    .map(board -> objectMapper.convertValue(board, Board.class))
                    .toList();
        }
        return new ArrayList<>();
    }

    public void setBoardTop3Values(List<Board> boardList) {
        redisTemplate.opsForValue().set(boardKey, boardList);
    }
}
