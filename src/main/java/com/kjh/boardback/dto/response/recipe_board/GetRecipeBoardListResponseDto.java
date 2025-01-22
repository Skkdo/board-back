package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeBoardListItem;
import com.kjh.boardback.entity.recipe_board.RecipeBoard;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeBoardListResponseDto {
    private final List<RecipeBoardListItem> boardList;

    public GetRecipeBoardListResponseDto(List<RecipeBoard> boardList) {
        this.boardList = RecipeBoardListItem.getList(boardList);
    }
}
