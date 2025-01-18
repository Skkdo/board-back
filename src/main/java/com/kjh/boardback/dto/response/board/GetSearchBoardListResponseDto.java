package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.BoardListItem;
import com.kjh.boardback.entity.board.Board;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetSearchBoardListResponseDto extends ResponseDto {

    private List<BoardListItem> searchList;

    public GetSearchBoardListResponseDto(List<Board> boardList) {
        super(ResponseCode.SUCCESS);
        this.searchList = BoardListItem.getList(boardList);
    }

}
