package com.kjh.boardback.domain.recipe_board.dto.response;

import com.kjh.boardback.domain.recipe_board.dto.object.RecipeFavoriteListItem;
import com.kjh.boardback.domain.recipe_board.entity.RecipeFavorite;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeFavoriteListResponseDto {

    private final List<RecipeFavoriteListItem> favoriteList;

    public GetRecipeFavoriteListResponseDto(List<RecipeFavorite> favoriteList) {
        this.favoriteList = RecipeFavoriteListItem.getList(favoriteList);
    }
}
