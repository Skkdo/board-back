package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeBoardListItem;
import com.kjh.boardback.entity.recipe_board.RecipeBoard;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTop3GeneralRecipeBoardListResponseDto extends ResponseDto {

    private List<RecipeBoardListItem> generalrecipetop3List;

    public GetTop3GeneralRecipeBoardListResponseDto(List<RecipeBoard> boardList) {
        super(ResponseCode.SUCCESS);
        this.generalrecipetop3List = RecipeBoardListItem.getList(boardList);
    }
}
