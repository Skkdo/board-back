package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.BoardListItem;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.board.Board;
import java.util.List;
import lombok.Getter;

@Getter
public class GetBoardListResponseDto {
    private final List<BoardListItem> boardList;

    public GetBoardListResponseDto(List<Board> boardList) {
        this.boardList = BoardListItem.getList(boardList);
    }

    public GetBoardListResponseDto(List<Board> boardList, User user) {
        this.boardList = BoardListItem.getList(boardList,user);
    }
}
