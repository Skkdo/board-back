package com.kjh.boardback.domain.trade_board.service;

import com.kjh.boardback.domain.trade_board.dto.response.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import com.kjh.boardback.domain.trade_board.entity.TradeFavorite;
import com.kjh.boardback.domain.trade_board.repository.TradeBoardRepository;
import com.kjh.boardback.domain.trade_board.repository.TradeFavoriteRepository;
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
public class TradeFavoriteService {

    private final TradeFavoriteRepository favoriteRepository;
    private final TradeBoardRepository boardRepository;
    private final UserService userService;

    public GetTradeFavoriteListResponseDto getFavoriteList(Integer boardNumber) {
        findByBoardNumber(boardNumber);
        List<TradeFavorite> favoriteList = favoriteRepository.getFavoriteList(boardNumber);
        return new GetTradeFavoriteListResponseDto(favoriteList);
    }

    public TradeBoard findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public void deleteByBoardNumber(Integer boardNumber) {
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
    }

    @Transactional
    public void putFavorite(String email, Integer boardNumber) {

        User user = userService.findByEmailOrElseThrow(email);
        TradeBoard board = findByBoardNumber(boardNumber);

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

        boardRepository.save(board);
    }
}
