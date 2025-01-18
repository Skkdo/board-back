package com.kjh.boardback.dto.response.trade_board;

import com.kjh.boardback.dto.object.TradeFavoriteListItem;
import com.kjh.boardback.entity.trade_board.TradeFavorite;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTradeFavoriteListResponseDto extends ResponseDto {

    private final List<TradeFavoriteListItem> favoriteList;

    public GetTradeFavoriteListResponseDto(List<TradeFavorite> favoriteList) {
        super(ResponseCode.SUCCESS);
        this.favoriteList = TradeFavoriteListItem.getList(favoriteList);
    }
}
