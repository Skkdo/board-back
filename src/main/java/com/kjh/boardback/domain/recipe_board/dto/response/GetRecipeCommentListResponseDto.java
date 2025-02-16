package com.kjh.boardback.domain.recipe_board.dto.response;

import com.kjh.boardback.domain.recipe_board.dto.object.RecipeCommentListItem;
import com.kjh.boardback.domain.recipe_board.entity.RecipeComment;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeCommentListResponseDto {

    private List<RecipeCommentListItem> commentList;

    public GetRecipeCommentListResponseDto(List<RecipeComment> commentList) {
        this.commentList = RecipeCommentListItem.getList(commentList);
    }
}
