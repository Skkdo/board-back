package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.board.Favorite;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteListItem {
    private String email;
    private String nickname;
    private String profileImage;

    public FavoriteListItem(Favorite favorite) {
        this.email = favorite.getUser().getEmail();
        this.nickname = favorite.getUser().getNickname();
        this.profileImage = favorite.getUser().getProfileImage();
    }

    public static List<FavoriteListItem> getList(List<Favorite> favoriteList) {
        return favoriteList.stream()
                .map(FavoriteListItem::new)
                .toList();
    }
}
