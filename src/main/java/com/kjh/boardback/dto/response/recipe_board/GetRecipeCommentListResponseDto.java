package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeCommentListItem;
import com.kjh.boardback.entity.recipe_board.RecipeComment;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeCommentListResponseDto extends ResponseDto {

    private List<RecipeCommentListItem> commentList;

    public GetRecipeCommentListResponseDto(List<RecipeComment> commentList) {
        super(ResponseCode.SUCCESS);
        this.commentList = RecipeCommentListItem.getList(commentList);
    }
}
