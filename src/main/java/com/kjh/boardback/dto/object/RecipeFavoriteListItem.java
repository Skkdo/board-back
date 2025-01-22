package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.recipe_board.RecipeFavorite;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFavoriteListItem {
    private String email;
    private String nickname;
    private String profileImage;

    public RecipeFavoriteListItem(RecipeFavorite favorite) {
        this.email = favorite.getUser().getEmail();
        this.nickname = favorite.getUser().getNickname();
        this.profileImage = favorite.getUser().getProfileImage();
    }

    public static List<RecipeFavoriteListItem> getList(List<RecipeFavorite> favoriteList) {
        return favoriteList.stream().map(RecipeFavoriteListItem::new).toList();
    }
}
