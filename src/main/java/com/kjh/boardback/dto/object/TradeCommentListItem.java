package com.kjh.boardback.dto.object;

import com.kjh.boardback.entity.trade_board.TradeComment;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TradeCommentListItem {
    private Integer commentNumber;
    private String nickname;
    private String profileImage;
    private LocalDateTime writeDatetime;
    private String content;

    private TradeCommentListItem(TradeComment comment) {
        this.commentNumber = comment.getCommentNumber();
        this.nickname = comment.getWriter().getNickname();
        this.profileImage = comment.getWriter().getProfileImage();
        this.writeDatetime = comment.getCreatedAt();
        this.content = comment.getContent();
    }

    public static List<TradeCommentListItem> copyList(List<TradeComment> commentList) {
        return commentList.stream().map(TradeCommentListItem::new).toList();
    }
}
