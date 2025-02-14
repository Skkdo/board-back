package com.kjh.boardback.domain.recipe_board.service;

import com.kjh.boardback.domain.recipe_board.dto.request.PatchRecipeCommentRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.request.PostRecipeCommentRequestDto;
import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeCommentListResponseDto;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.entity.RecipeComment;
import com.kjh.boardback.domain.recipe_board.repository.RecipeCommentRepository;
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
public class RecipeCommentService {

    private final RecipeCommentRepository commentRepository;
    private final RecipeBoardService boardService;
    private final UserService userService;

    public RecipeComment findByCommentNumber(Integer commentNumber) {
        return commentRepository.findByCommentNumber(commentNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_COMMENT));
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        commentRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    public GetRecipeCommentListResponseDto getCommentList(Integer boardNumber) {
        boardService.findByBoardNumber(boardNumber);
        List<RecipeComment> commentList = commentRepository.getCommentListWithUser(boardNumber);
        return new GetRecipeCommentListResponseDto(commentList);
    }

    @Transactional
    public void postComment(Integer boardNumber, String email, PostRecipeCommentRequestDto dto) {

        RecipeBoard board = boardService.findByBoardNumber(boardNumber);
        User user = userService.findByEmailOrElseThrow(email);

        RecipeComment comment = RecipeComment.from(board, user, dto);
        commentRepository.save(comment);

        board.increaseCommentCount();
        boardService.save(board);
    }

    @Transactional
    public void patchComment(Integer boardNumber, Integer commentNumber, String email,
                             PatchRecipeCommentRequestDto dto) {

        userService.findByEmailOrElseThrow(email);
        boardService.findByBoardNumber(boardNumber);
        RecipeComment comment = findByCommentNumber(commentNumber);

        String commentWriterEmail = comment.getWriter().getEmail();
        boolean isCommentWriter = commentWriterEmail.equals(email);
        if (!isCommentWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        comment.patchComment(dto);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer boardNumber, Integer commentNumber, String email) {

        userService.findByEmailOrElseThrow(email);
        RecipeBoard board = boardService.findByBoardNumber(boardNumber);
        RecipeComment comment = findByCommentNumber(commentNumber);

        String boardWriterEmail = board.getWriter().getEmail();
        String commentWriterEmail = comment.getWriter().getEmail();

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
