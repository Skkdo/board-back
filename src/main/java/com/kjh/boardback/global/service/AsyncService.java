package com.kjh.boardback.global.service;

import com.kjh.boardback.domain.board.dto.object.BoardDto;
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
    public void updateTop3IfNeed(BoardDto boardDto) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        if (boardDto.getCreatedAt().isBefore(sevenDaysAgo)) {
            return;
        }

        List<BoardDto> boardDtoList = redisService.getBoardTop3();
        int boardNumber = boardDto.getBoardNumber();

        int index = search(boardDtoList, boardNumber);
        if (index < 0) {
            boardDtoList.add(boardDto);
            sort(boardDtoList);
            boardDtoList.remove(boardDtoList.size() - 1);
        }else {
            boardDtoList.set(index,boardDto);
            sort(boardDtoList);
        }
        redisService.setBoardTop3(boardDtoList);
    }

    private static void sort(List<BoardDto> boardDtoList) {
        boardDtoList.sort(Comparator.comparingInt(
                (BoardDto b) -> -b.getViewCount()
        ).thenComparingInt(
                (BoardDto b) -> -b.getFavoriteCount()
        ));
    }

    @Async("taskExecutor")
    public void patchBoardIfTop3(BoardDto boardDto) {
        List<BoardDto> boardDtoList = redisService.getBoardTop3();
        int boardNumber = boardDto.getBoardNumber();
        
        int index = search(boardDtoList, boardNumber);
        if(index < 0) return;

        boardDtoList.set(index,boardDto);
        redisService.setBoardTop3(boardDtoList);
    }

    @Async("taskExecutor")
    public void deleteBoardIfTop3(Integer boardNumber) {
        List<BoardDto> boardDtoList = redisService.getBoardTop3();
        
        int index = search(boardDtoList, boardNumber);
        if(index < 0) return;

        boardDtoList.remove(index);
        redisService.setBoardTop3(boardDtoList);
    }
    
    private int search(List<BoardDto> boardDtoList, int boardNumber) {
        for (int i = 0; i < boardDtoList.size(); i++) {
            BoardDto top3Board = boardDtoList.get(i);
            if (top3Board.getBoardNumber() == boardNumber) {
                return i;
            }
        }
        return -1;
    }
}
