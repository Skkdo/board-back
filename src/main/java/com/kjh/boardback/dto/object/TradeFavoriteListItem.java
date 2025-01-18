package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.trade_board.TradeFavorite;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TradeFavoriteListItem {
    private String email;
    private String nickname;
    private String profileImage;

    public TradeFavoriteListItem(TradeFavorite favorite) {
        this.email = favorite.getUser().getEmail();
        this.nickname = favorite.getUser().getNickname();
        this.profileImage = favorite.getUser().getProfileImage();
    }

    public static List<TradeFavoriteListItem> getList(List<TradeFavorite> favoriteList) {
        return favoriteList.stream().map(TradeFavoriteListItem::new).toList();
    }
}
