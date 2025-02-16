package com.kjh.boardback.domain.board.dto.response;

import com.kjh.boardback.domain.board.dto.object.CommentListItem;
import com.kjh.boardback.domain.board.entity.Comment;
import java.util.List;
import lombok.Getter;

@Getter
public class GetCommentListResponseDto {

    private List<CommentListItem> commentList;

    public GetCommentListResponseDto(List<Comment> commentList) {
        this.commentList = CommentListItem.getList(commentList);
    }
}
