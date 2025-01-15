package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.recipe_board.RecipeComment;
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

    private RecipeCommentListItem(RecipeComment comment) {
        this.commentNumber = comment.getCommentNumber();
        this.nickname = comment.getWriter().getNickname();
        this.profileImage = comment.getWriter().getProfileImage();
        this.writeDatetime = comment.getCreatedAt().toString();
        this.content = comment.getContent();
    }

    public static List<RecipeCommentListItem> getList(List<RecipeComment> commentList) {
        return commentList.stream().map(RecipeCommentListItem::new).toList();
    }
}
