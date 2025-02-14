package com.kjh.boardback.domain.trade_board.service;

import com.kjh.boardback.domain.trade_board.dto.response.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import com.kjh.boardback.domain.trade_board.entity.TradeFavorite;
import com.kjh.boardback.domain.trade_board.repository.TradeFavoriteRepository;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeFavoriteService {

    private final TradeFavoriteRepository favoriteRepository;
    private final TradeBoardService boardService;
    private final UserService userService;

    public GetTradeFavoriteListResponseDto getFavoriteList(Integer boardNumber) {
        boardService.findByBoardNumber(boardNumber);
        List<TradeFavorite> favoriteList = favoriteRepository.getFavoriteList(boardNumber);
        return new GetTradeFavoriteListResponseDto(favoriteList);
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        User user = userService.findByEmailOrElseThrow(email);
        TradeBoard board = boardService.findByBoardNumber(boardNumber);

        Optional<TradeFavorite> optional = favoriteRepository.findByBoard_BoardNumberAndUser_Email(
                boardNumber, email);

        if (optional.isEmpty()) {
            TradeFavorite favorite = TradeFavorite.from(user, board);
            favoriteRepository.save(favorite);
            board.increaseFavoriteCount();
        } else {
            favoriteRepository.delete(optional.get());
            board.decreaseFavoriteCount();
        }

        boardService.save(board);
    }
}
