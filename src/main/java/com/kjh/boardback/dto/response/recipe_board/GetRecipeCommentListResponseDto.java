package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeCommentListItem;
import com.kjh.boardback.entity.recipe_board.RecipeComment;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeCommentListResponseDto {

    private List<RecipeCommentListItem> commentList;

    public GetRecipeCommentListResponseDto(List<RecipeComment> commentList) {
        this.commentList = RecipeCommentListItem.getList(commentList);
    }
}
