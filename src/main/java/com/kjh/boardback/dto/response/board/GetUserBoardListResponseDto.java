package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.BoardListItem;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.board.Board;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetUserBoardListResponseDto extends ResponseDto {

    private List<BoardListItem> userBoardList;

    public GetUserBoardListResponseDto(List<Board> boardList, User user) {
        super(ResponseCode.SUCCESS);
        this.userBoardList = BoardListItem.getList(boardList, user);
    }
}
