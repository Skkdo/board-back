package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.CommentListItem;
import com.kjh.boardback.entity.board.Comment;
import java.util.List;
import lombok.Getter;

@Getter
public class GetCommentListResponseDto {

    private List<CommentListItem> commentList;

    public GetCommentListResponseDto(List<Comment> commentList) {
        this.commentList = CommentListItem.getList(commentList);
    }
}
