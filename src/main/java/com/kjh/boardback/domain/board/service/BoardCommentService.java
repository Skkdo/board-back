package com.kjh.boardback.domain.board.service;

import com.kjh.boardback.domain.board.dto.request.PatchCommentRequestDto;
import com.kjh.boardback.domain.board.dto.request.PostCommentRequestDto;
import com.kjh.boardback.domain.board.dto.response.GetCommentListResponseDto;
import com.kjh.boardback.domain.board.entity.Board;
import com.kjh.boardback.domain.board.entity.Comment;
import com.kjh.boardback.domain.board.repository.CommentRepository;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final UserService userService;

    public Comment findByCommentNumber(Integer commentNumber) {
        return commentRepository.findByCommentNumber(commentNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_COMMENT));
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        commentRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    public GetCommentListResponseDto getCommentList(Integer boardNumber) {

        boardService.findByBoardNumber(boardNumber);
        List<Comment> commentList = commentRepository.getCommentList(boardNumber);

        return new GetCommentListResponseDto(commentList);
    }

    @Transactional
    public void postComment(Integer boardNumber, String email, PostCommentRequestDto dto) {
        Board board = boardService.findByBoardNumber(boardNumber);
        User user = userService.findByEmailOrElseThrow(email);

        Comment comment = new Comment(board, user, dto);
        commentRepository.save(comment);

        board.increaseCommentCount();
        boardService.save(board);
    }

    @Transactional
    public void patchComment(Integer boardNumber, Integer commentNumber, String email, PatchCommentRequestDto dto) {

        userService.findByEmailOrElseThrow(email);
        boardService.findByBoardNumber(boardNumber);

        Comment comment = findByCommentNumber(commentNumber);
        String commentWriterEmail = comment.getWriter().getEmail();
        boolean isCommentWriter = commentWriterEmail.equals(email);
        if (!isCommentWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        comment.patchComment(dto);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer boardNumber, String email, Integer commentNumber) {

        Board board = boardService.findByBoardNumber(boardNumber);
        userService.findByEmailOrElseThrow(email);
        Comment comment = findByCommentNumber(commentNumber);

        String writerEmail = board.getWriter().getEmail();
        String commentWriterEmail = comment.getWriter().getEmail();

        boolean isWriter = writerEmail.equals(email);
        boolean isCommentWriter = commentWriterEmail.equals(email);
        if (!isWriter && !isCommentWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        commentRepository.delete(comment);
        board.decreaseCommentCount();
        boardService.save(board);
    }
}
