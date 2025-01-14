package com.kjh.boardback.dto.object;

import com.kjh.boardback.repository.resultSet.GetRecipeFavoriteListResultSet;
import java.util.ArrayList;
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

    public RecipeFavoriteListItem(GetRecipeFavoriteListResultSet resultSet) {
        this.email = resultSet.getEmail();
        this.nickname = resultSet.getNickname();
        this.profileImage = resultSet.getProfileImage();
    }

    public static List<RecipeFavoriteListItem> copyList(List<GetRecipeFavoriteListResultSet> resultSets) {
        List<RecipeFavoriteListItem> list = new ArrayList<>();
        for (GetRecipeFavoriteListResultSet resultSet : resultSets) {
            RecipeFavoriteListItem favoriteListItem = new RecipeFavoriteListItem(resultSet);
            list.add(favoriteListItem);
        }
        return list;
    }
}
