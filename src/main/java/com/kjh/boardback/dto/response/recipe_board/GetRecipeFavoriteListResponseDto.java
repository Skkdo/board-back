package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeFavoriteListItem;
import com.kjh.boardback.entity.recipe_board.RecipeFavorite;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeFavoriteListResponseDto {

    private final List<RecipeFavoriteListItem> favoriteList;

    public GetRecipeFavoriteListResponseDto(List<RecipeFavorite> favoriteList) {
        this.favoriteList = RecipeFavoriteListItem.getList(favoriteList);
    }
}
