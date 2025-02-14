package com.kjh.boardback.domain.board.dto.response;

import com.kjh.boardback.domain.board.dto.object.BoardListItem;
import com.kjh.boardback.domain.board.entity.Board;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class GetBoardPageListResponseDto {
    private Page<BoardListItem> boardList;

    public GetBoardPageListResponseDto(Page<Board> boardList) {
        this.boardList = BoardListItem.getList(boardList);
    }
}
