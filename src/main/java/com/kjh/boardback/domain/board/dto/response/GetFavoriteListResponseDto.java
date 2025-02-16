package com.kjh.boardback.domain.board.dto.response;

import com.kjh.boardback.domain.board.dto.object.FavoriteListItem;
import com.kjh.boardback.domain.board.entity.Favorite;
import java.util.List;
import lombok.Getter;

@Getter
public class GetFavoriteListResponseDto {

    private final List<FavoriteListItem> favoriteList;

    public GetFavoriteListResponseDto(List<Favorite> favoriteList) {
        this.favoriteList = FavoriteListItem.getList(favoriteList);
    }
}
