package com.kjh.boardback.domain.recipe_board.service;

import com.kjh.boardback.domain.recipe_board.dto.response.GetRecipeFavoriteListResponseDto;
import com.kjh.boardback.domain.recipe_board.entity.RecipeBoard;
import com.kjh.boardback.domain.recipe_board.entity.RecipeFavorite;
import com.kjh.boardback.domain.recipe_board.repository.RecipeFavoriteRepository;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipeFavoriteService {

    private final RecipeFavoriteRepository favoriteRepository;
    private final RecipeBoardService boardService;
    private final UserService userService;

    public GetRecipeFavoriteListResponseDto getFavoriteList(Integer boardNumber) {
        boardService.findByBoardNumber(boardNumber);
        List<RecipeFavorite> favoriteList = favoriteRepository.getFavoriteListWithUser(boardNumber);
        return new GetRecipeFavoriteListResponseDto(favoriteList);
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        User user = userService.findByEmailOrElseThrow(email);
        RecipeBoard board = boardService.findByBoardNumber(boardNumber);

        Optional<RecipeFavorite> optional = favoriteRepository.findByBoard_BoardNumberAndUser_Email(
                boardNumber, email);

        if (optional.isEmpty()) {
            RecipeFavorite favorite = RecipeFavorite.from(user, board);
            favoriteRepository.save(favorite);
            board.increaseFavoriteCount();
        } else {
            favoriteRepository.delete(optional.get());
            board.decreaseFavoriteCount();
        }
        boardService.save(board);
    }
}
