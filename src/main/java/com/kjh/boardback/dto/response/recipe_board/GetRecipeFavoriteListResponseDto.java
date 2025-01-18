package com.kjh.boardback.dto.response.recipe_board;

import com.kjh.boardback.dto.object.RecipeFavoriteListItem;
import com.kjh.boardback.entity.board.Favorite;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetRecipeFavoriteListResponseDto extends ResponseDto {

    private final List<RecipeFavoriteListItem> favoriteList;

    public GetRecipeFavoriteListResponseDto(List<Favorite> favoriteList) {
        super(ResponseCode.SUCCESS);
        this.favoriteList = RecipeFavoriteListItem.getList(favoriteList);
    }
}
