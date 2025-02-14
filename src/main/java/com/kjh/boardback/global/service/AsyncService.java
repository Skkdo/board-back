package com.kjh.boardback.global.service;

import com.kjh.boardback.domain.board.entity.Board;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final RedisService redisService;

    @Async("taskExecutor")
    public void updateTop3IfNeed(Board board) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        if (board.getCreatedAt().isBefore(sevenDaysAgo)) {
            return;
        }

        List<Board> boardList = redisService.getBoardTop3Values();
        Board last = boardList.get(boardList.size() - 1);

        if (last.getViewCount() <= board.getViewCount()) {
            boardList.add(board);

            boardList.sort(Comparator.comparingInt(
                    (Board b) -> -b.getViewCount()
            ).thenComparingInt(
                    (Board b) -> -b.getFavoriteCount()
            ));
        }
    }

    @Async("taskExecutor")
    public void patchBoardIfTop3(Board board) {
        List<Board> boardList = redisService.getBoardTop3Values();
        for (int i = 0; i < boardList.size(); i++) {
            Board top3Board = boardList.get(i);
            if (top3Board.getBoardNumber() == board.getBoardNumber()) {
                boardList.set(i, board);
                redisService.setBoardTop3Values(boardList);
                return;
            }
        }
    }

    @Async("taskExecutor")
    public void deleteBoardIfTop3(Integer boardNumber) {
        List<Board> boardList = redisService.getBoardTop3Values();
        for (int i = 0; i < boardList.size(); i++) {
            Board top3Board = boardList.get(i);
            if (top3Board.getBoardNumber() == boardNumber) {
                boardList.remove(i);
                redisService.setBoardTop3Values(boardList);
                return;
            }
        }
    }
}
