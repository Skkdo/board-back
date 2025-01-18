package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.BoardListItem;
import com.kjh.boardback.entity.board.Board;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetLatestBoardListResponseDto extends ResponseDto {

    private final List<BoardListItem> latestList;

    public GetLatestBoardListResponseDto(List<Board> boardList) {
        super(ResponseCode.SUCCESS);
        this.latestList = BoardListItem.getList(boardList);
    }

}
