package com.kjh.boardback.domain.recipe_board.service;

import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.entity.RecipeFavorite;
import com.kjh.boardback.domain.recipe_board.entity.RecipeFavoritePk;
import com.kjh.boardback.domain.recipe_board.repository.RecipeBoardRepository;
import com.kjh.boardback.domain.recipe_board.repository.RecipeFavoriteRepository;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipeFavoriteService {

    private final RecipeFavoriteRepository favoriteRepository;
    private final RecipeBoardRepository boardRepository;
    private final UserService userService;

    public GetRecipeFavoriteListResponseDto getFavoriteList(Integer boardNumber) {
        findByBoardNumber(boardNumber);
        List<RecipeFavorite> favoriteList = favoriteRepository.getFavoriteListWithUser(boardNumber);
        return new GetRecipeFavoriteListResponseDto(favoriteList);
    }

    public RecipeBoard findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        User user = userService.findByEmailOrElseThrow(email);
        RecipeBoard board = findByBoardNumber(boardNumber);

        Optional<RecipeFavorite> optional = favoriteRepository.findByBoard_BoardNumberAndUser_Email(
                boardNumber, email);

        if (optional.isEmpty()) {
            RecipeFavoritePk recipeFavoritePk = new RecipeFavoritePk(user.getEmail(), board.getBoardNumber());
            RecipeFavorite favorite = new RecipeFavorite(recipeFavoritePk, user, board);
            favoriteRepository.save(favorite);
            board.increaseFavoriteCount();
        } else {
            favoriteRepository.delete(optional.get());
            board.decreaseFavoriteCount();
        }
        boardRepository.save(board);
    }
}
