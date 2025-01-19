package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.FavoriteListItem;
import com.kjh.boardback.entity.board.Favorite;
import java.util.List;
import lombok.Getter;

@Getter
public class GetFavoriteListResponseDto {

    private final List<FavoriteListItem> favoriteList;

    public GetFavoriteListResponseDto(List<Favorite> favoriteList) {
        this.favoriteList = FavoriteListItem.getList(favoriteList);
    }
}
