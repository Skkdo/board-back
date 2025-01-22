package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.dto.object.TradeFavoriteListItem;
import com.kjh.boardback.entity.trade_board.TradeFavorite;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeFavoriteListResponseDto {

    private final List<TradeFavoriteListItem> favoriteList;

    public GetTradeFavoriteListResponseDto(List<TradeFavorite> favoriteList) {
        this.favoriteList = TradeFavoriteListItem.getList(favoriteList);
    }
}
