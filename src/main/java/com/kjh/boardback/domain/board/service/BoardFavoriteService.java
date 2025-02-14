package com.kjh.boardback.domain.board.service;

import com.kjh.boardback.domain.board.dto.response.GetFavoriteListResponseDto;
import com.kjh.boardback.domain.board.entity.Board;
import com.kjh.boardback.domain.board.entity.Favorite;
import com.kjh.boardback.domain.board.repository.FavoriteRepository;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.service.AsyncService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final BoardService boardService;
    private final UserService userService;
    private final AsyncService asyncService;

    public GetFavoriteListResponseDto getFavoriteList(Integer boardNumber) {

        boardService.findByBoardNumber(boardNumber);
        List<Favorite> favoriteList = favoriteRepository.getFavoriteList(boardNumber);

        return new GetFavoriteListResponseDto(favoriteList);
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        Board board = boardService.findByBoardNumber(boardNumber);
        User user = userService.findByEmailOrElseThrow(email);

        Optional<Favorite> optional = favoriteRepository.findByBoard_BoardNumberAndUser_Email(
                boardNumber, email);

        if (optional.isEmpty()) {
            Favorite favorite = new Favorite(board, user);
            favoriteRepository.save(favorite);
            board.increaseFavoriteCount();
        } else {
            Favorite favorite = optional.get();
            favoriteRepository.delete(favorite);
            board.decreaseFavoriteCount();
        }
        boardService.save(board);
        asyncService.updateTop3IfNeed(board);
    }
}
