package com.kjh.boardback.domain.recipe_board.dto.object;

import com.kjh.boardback.domain.recipe_board.entity.RecipeComment;
import java.time.LocalDateTime;
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
    private LocalDateTime writeDatetime;
    private String content;

    private RecipeCommentListItem(RecipeComment comment) {
        this.commentNumber = comment.getCommentNumber();
        this.nickname = comment.getWriter().getNickname();
        this.profileImage = comment.getWriter().getProfileImage();
        this.writeDatetime = comment.getCreatedAt();
        this.content = comment.getContent();
    }

    public static List<RecipeCommentListItem> getList(List<RecipeComment> commentList) {
        return commentList.stream().map(RecipeCommentListItem::new).toList();
    }
}
