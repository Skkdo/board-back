package com.kjh.boardback.domain.trade_board.service;

import com.kjh.boardback.domain.trade_board.dto.request.PatchTradeCommentRequestDto;
import com.kjh.boardback.domain.trade_board.dto.request.PostTradeCommentRequestDto;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeCommentListResponseDto;
import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import com.kjh.boardback.domain.trade_board.entity.TradeComment;
import com.kjh.boardback.domain.trade_board.repository.TradeCommentRepository;
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
public class TradeCommentService {

    private final TradeCommentRepository commentRepository;
    private final TradeBoardService boardService;
    private final UserService userService;

    public TradeComment findByCommentNumber(Integer commentNumber) {
        return commentRepository.findByCommentNumber(commentNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_COMMENT));
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        commentRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    public GetTradeCommentListResponseDto getCommentList(Integer boardNumber) {
        boardService.findByBoardNumber(boardNumber);
        List<TradeComment> commentList = commentRepository.getCommentList(boardNumber);
        return new GetTradeCommentListResponseDto(commentList);
    }

    @Transactional
    public void postComment(Integer boardNumber, String email, PostTradeCommentRequestDto dto) {

        TradeBoard board = boardService.findByBoardNumber(boardNumber);
        User user = userService.findByEmailOrElseThrow(email);

        TradeComment comment = TradeComment.from(user, board, dto);
        commentRepository.save(comment);

        board.increaseCommentCount();
        boardService.save(board);
    }

    @Transactional
    public void patchComment(Integer boardNumber, Integer commentNumber, String email,
                             PatchTradeCommentRequestDto dto) {

        userService.findByEmailOrElseThrow(email);
        boardService.findByBoardNumber(boardNumber);
        TradeComment comment = findByCommentNumber(commentNumber);

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

        userService.findByEmailOrElseThrow(email);
        TradeBoard board = boardService.findByBoardNumber(boardNumber);
        TradeComment comment = findByCommentNumber(commentNumber);

        String commentWriterEmail = comment.getWriter().getEmail();
        String boardWriterEmail = board.getWriter().getEmail();

        boolean isBoardWriter = boardWriterEmail.equals(email);
        boolean isCommentWriter = commentWriterEmail.equals(email);
        if (!isCommentWriter && !isBoardWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        commentRepository.delete(comment);
        board.decreaseCommentCount();
        boardService.save(board);
    }
}
