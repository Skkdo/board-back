package com.kjh.boardback.dto.object;

import com.kjh.boardback.repository.resultSet.GetRecipeCommentListResultSet;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class RecipeCommentListItem {
    private Integer commentNumber;
    private String nickname;
    private String profileImage;
    private String writeDatetime;
    private String content;

    private RecipeCommentListItem(GetRecipeCommentListResultSet resultSet) {
        this.commentNumber = resultSet.getCommentNumber();
        this.nickname = resultSet.getNickname();
        this.profileImage = resultSet.getProfileImage();
        this.writeDatetime = resultSet.getWriteDatetime();
        this.content = resultSet.getContent();
    }

    public static List<RecipeCommentListItem> copyList(List<GetRecipeCommentListResultSet> resultSets) {
        List<RecipeCommentListItem> list = new ArrayList<>();
        for (GetRecipeCommentListResultSet resultSet : resultSets) {
            RecipeCommentListItem commentListItem = new RecipeCommentListItem(resultSet);
            list.add(commentListItem);
        }
        return list;
    }
}
