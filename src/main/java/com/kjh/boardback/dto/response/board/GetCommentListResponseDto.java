package com.kjh.boardback.dto.response.board;

import com.kjh.boardback.dto.object.CommentListItem;
import com.kjh.boardback.entity.board.Comment;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import java.util.List;
import lombok.Getter;

@Getter
public class GetCommentListResponseDto extends ResponseDto {

    private List<CommentListItem> commentList;

    public GetCommentListResponseDto(List<Comment> commentList) {
        super(ResponseCode.SUCCESS);
        this.commentList = CommentListItem.getList(commentList);
    }
}
