package com.kjh.boardback.domain.board.dto.response;

import com.kjh.boardback.domain.board.dto.object.BoardDto;
import com.kjh.boardback.domain.board.dto.object.BoardListItem;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.board.entity.Board;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GetBoardListResponseDto {
    private final List<BoardListItem> boardList;

    private GetBoardListResponseDto(List<BoardListItem> boardList) {
        this.boardList = boardList;
    }

    public static GetBoardListResponseDto from(List<Board> boardList) {
        return new GetBoardListResponseDto(BoardListItem.getList(boardList));
    }

    public static GetBoardListResponseDto from(List<Board> boardList, User user) {
        return new GetBoardListResponseDto(BoardListItem.getList(boardList,user));
    }

    public static GetBoardListResponseDto fromDto(List<BoardDto> dtoList) {
        return new GetBoardListResponseDto(BoardListItem.getListByDto(dtoList));
    }
}
