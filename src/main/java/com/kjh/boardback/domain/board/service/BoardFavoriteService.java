package com.kjh.boardback.domain.board.service;

import com.kjh.boardback.domain.board.dto.response.GetFavoriteListResponseDto;
import com.kjh.boardback.domain.board.entity.Board;
import com.kjh.boardback.domain.board.entity.Favorite;
import com.kjh.boardback.domain.board.entity.FavoritePk;
import com.kjh.boardback.domain.board.repository.BoardRepository;
import com.kjh.boardback.domain.board.repository.FavoriteRepository;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
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
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final AsyncService asyncService;

    public GetFavoriteListResponseDto getFavoriteList(Integer boardNumber) {

        findByBoardNumber(boardNumber);
        List<Favorite> favoriteList = favoriteRepository.getFavoriteList(boardNumber);

        return new GetFavoriteListResponseDto(favoriteList);
    }

    public Board findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        Board board = findByBoardNumber(boardNumber);
        User user = userService.findByEmailOrElseThrow(email);

        Optional<Favorite> optional = favoriteRepository.findByBoard_BoardNumberAndUser_Email(
                boardNumber, email);

        if (optional.isEmpty()) {
            FavoritePk favoritePk = new FavoritePk(user.getEmail(), board.getBoardNumber());
            Favorite favorite = new Favorite(favoritePk, user, board);
            favoriteRepository.save(favorite);
            board.increaseFavoriteCount();
        } else {
            Favorite favorite = optional.get();
            favoriteRepository.delete(favorite);
            board.decreaseFavoriteCount();
        }
        boardRepository.save(board);
        asyncService.updateTop3IfNeed(board);
    }
}
