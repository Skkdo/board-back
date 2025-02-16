package com.kjh.boardback.domain.recipe_board.dto.response;

import com.kjh.boardback.domain.recipe_board.dto.object.RecipeBoardListItem;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeBoardListResponseDto {
    private final List<RecipeBoardListItem> boardList;

    public GetRecipeBoardListResponseDto(List<RecipeBoard> boardList) {
        this.boardList = RecipeBoardListItem.getList(boardList);
    }
}
