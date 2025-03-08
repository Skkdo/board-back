package com.kjh.boardback.global.service;

import com.kjh.boardback.domain.board.dto.object.BoardDto;
import com.kjh.boardback.domain.board.entity.Board;
import com.kjh.boardback.domain.board.repository.BoardRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardTop3UpdateScheduler {

    private final RedisService redisService;
    private final BoardRepository boardRepository;

    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Seoul")
    public void updateBoardTop3() {
        Pageable pageable = PageRequest.of(0, 3);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<Board> top3List = boardRepository.getTop3Within7Days(sevenDaysAgo, pageable);
        List<BoardDto> dtoList = BoardDto.getList(top3List);
        redisService.setBoardTop3(dtoList);
    }
}
