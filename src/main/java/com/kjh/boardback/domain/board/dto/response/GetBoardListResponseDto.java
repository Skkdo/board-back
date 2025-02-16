package com.kjh.boardback.domain.board.dto.response;

import com.kjh.boardback.domain.board.dto.object.BoardListItem;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.board.entity.Board;
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
