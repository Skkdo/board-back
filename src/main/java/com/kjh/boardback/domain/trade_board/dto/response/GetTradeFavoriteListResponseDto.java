package com.kjh.boardback.domain.trade_board.dto.response;

import com.kjh.boardback.domain.trade_board.dto.object.TradeFavoriteListItem;
import com.kjh.boardback.domain.trade_board.entity.TradeFavorite;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeFavoriteListResponseDto {

    private final List<TradeFavoriteListItem> favoriteList;

    public GetTradeFavoriteListResponseDto(List<TradeFavorite> favoriteList) {
        this.favoriteList = TradeFavoriteListItem.getList(favoriteList);
    }
}
