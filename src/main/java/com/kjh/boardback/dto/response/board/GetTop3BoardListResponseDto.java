package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.BoardListItem;
import com.kjh.boardback.entity.board.Board;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTop3BoardListResponseDto extends ResponseDto {

    private List<BoardListItem> top3List;

    public GetTop3BoardListResponseDto(List<Board> boardList) {
        super(ResponseCode.SUCCESS);
        this.top3List = BoardListItem.getList(boardList);

    }
}
