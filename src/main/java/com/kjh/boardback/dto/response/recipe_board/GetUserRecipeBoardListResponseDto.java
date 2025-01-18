package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeBoardListItem;
import com.kjh.boardback.entity.recipe_board.RecipeBoard;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetUserRecipeBoardListResponseDto extends ResponseDto {

    private List<RecipeBoardListItem> userBoardList;

    public GetUserRecipeBoardListResponseDto(List<RecipeBoard> boardList) {
        super(ResponseCode.SUCCESS);
        this.userBoardList = RecipeBoardListItem.getList(boardList);
    }
}
