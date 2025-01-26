package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.BoardListItem;
import com.kjh.boardback.entity.board.Board;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class GetBoardPageListResponseDto {
    private Page<BoardListItem> boardList;

    public GetBoardPageListResponseDto(Page<Board> boardList) {
        this.boardList = BoardListItem.getList(boardList);
    }
}
