package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.board.Comment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListItem {
    private Integer commentNumber;
    private String nickname;
    private String profileImage;
    private LocalDateTime writeDatetime;
    private String content;

    private CommentListItem(Comment comment) {
        this.commentNumber = comment.getCommentNumber();
        this.nickname = comment.getWriter().getNickname();
        this.profileImage = comment.getWriter().getProfileImage();
        this.writeDatetime = comment.getCreatedAt();
        this.content = comment.getContent();
    }

    public static List<CommentListItem> getList(List<Comment> commentList) {
        return commentList.stream()
                .map(CommentListItem::new)
                .toList();
    }
}
