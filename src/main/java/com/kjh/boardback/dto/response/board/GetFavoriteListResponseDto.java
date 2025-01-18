package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.FavoriteListItem;
import com.kjh.boardback.entity.board.Favorite;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetFavoriteListResponseDto extends ResponseDto {

    private final List<FavoriteListItem> favoriteList;

    public GetFavoriteListResponseDto(List<Favorite> favoriteList) {
        super(ResponseCode.SUCCESS);
        this.favoriteList = FavoriteListItem.getList(favoriteList);
    }
}
