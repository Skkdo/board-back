package com.kjh.boardback.domain.trade_board.service;

import com.kjh.boardback.domain.search_log.entity.SearchLog;
import com.kjh.boardback.domain.search_log.service.SearchLogService;
import com.kjh.boardback.domain.trade_board.dto.request.PatchTradeBoardRequestDto;
import com.kjh.boardback.domain.trade_board.dto.request.PostTradeBoardRequestDto;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeBoardListResponseDto;
import com.kjh.boardback.domain.trade_board.dto.response.GetTradeBoardResponseDto;
import com.kjh.boardback.domain.trade_board.entity.TradeBoard;
import com.kjh.boardback.domain.trade_board.entity.TradeImage;
import com.kjh.boardback.domain.trade_board.repository.TradeBoardRepository;
import com.kjh.boardback.domain.user.entity.User;
import com.kjh.boardback.domain.user.service.UserService;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeBoardService {

    private final TradeBoardRepository boardRepository;
    private final TradeCommentService commentService;
    private final TradeImageService imageService;
    private final TradeFavoriteService favoriteService;
    private final UserService userService;
    private final SearchLogService searchLogService;

    public TradeBoard findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public TradeBoard save(TradeBoard board) {
        return boardRepository.save(board);
    }

    public GetTradeBoardResponseDto getBoard(Integer boardNumber) {
        TradeBoard board = findByBoardNumber(boardNumber);
        List<TradeImage> imageList = imageService.findByBoardNumber(boardNumber);
        return new GetTradeBoardResponseDto(board, imageList);
    }

    public GetTradeBoardListResponseDto getUserBoardList(String email) {
        userService.findByEmailOrElseThrow(email);
        List<TradeBoard> boardList = boardRepository.findByWriter_EmailOrderByCreatedAtDesc(email);
        return new GetTradeBoardListResponseDto(boardList);
    }

    public GetTradeBoardListResponseDto getSearchBoardList(String searchWord, String preSearchWord) {

        List<TradeBoard> boardList = boardRepository.getBySearchWord(searchWord, searchWord);
        SearchLog searchLog = new SearchLog(searchWord, preSearchWord, false);
        searchLogService.save(searchLog);

        boolean relation = preSearchWord != null;
        if (relation) {
            searchLog = new SearchLog(preSearchWord, searchWord, relation);
            searchLogService.save(searchLog);
        }
        return new GetTradeBoardListResponseDto(boardList);
    }

    public GetTradeBoardListResponseDto getTop3BoardList() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Order.desc("viewCount"), Order.desc("favoriteCount")));
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<TradeBoard> boardList = boardRepository.getTop3Within7Days(sevenDaysAgo, pageable);
        return new GetTradeBoardListResponseDto(boardList);
    }

    public GetTradeBoardListResponseDto getLatestBoardList() {
        List<TradeBoard> latestList = boardRepository.getLatestBoardList();
        return new GetTradeBoardListResponseDto(latestList);
    }

    @Transactional
    public void increaseViewCount(Integer boardNumber) {
        TradeBoard board = findByBoardNumber(boardNumber);
        board.increaseViewCount();
        boardRepository.save(board);
    }

    @Transactional
    public void postBoard(PostTradeBoardRequestDto dto, String email) {
        User user = userService.findByEmailOrElseThrow(email);
        TradeBoard board = TradeBoard.from(dto, user);
        boardRepository.save(board);

        List<String> boardImageList = dto.getBoardImageList();
        List<TradeImage> imageEntities = new ArrayList<>();

        for (String image : boardImageList) {
            TradeImage imageEntity = TradeImage.from(board, image);
            imageEntities.add(imageEntity);
        }
        imageService.saveAll(imageEntities);
    }

    @Transactional
    public void patchBoard(PatchTradeBoardRequestDto dto, Integer boardNumber, String email) {
        TradeBoard board = findByBoardNumber(boardNumber);
        userService.findByEmailOrElseThrow(email);

        String writerEmail = board.getWriter().getEmail();
        boolean isWriter = writerEmail.equals(email);
        if (!isWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        board.patchBoard(dto);
        boardRepository.save(board);

        imageService.deleteByBoardNumber(boardNumber);
        List<TradeImage> imageList = new ArrayList<>();
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            TradeImage imageEntity = TradeImage.from(board, image);
            imageList.add(imageEntity);
        }
        imageService.saveAll(imageList);
    }

    @Transactional
    public void deleteBoard(Integer boardNumber, String email) {
        TradeBoard board = findByBoardNumber(boardNumber);
        userService.findByEmailOrElseThrow(email);

        String writerEmail = board.getWriter().getEmail();
        boolean isWriter = writerEmail.equals(email);
        if (!isWriter) {
            throw new BusinessException(ResponseCode.NO_PERMISSION);
        }

        imageService.deleteByBoardNumber(boardNumber);
        favoriteService.deleteByBoardNumber(boardNumber);
        commentService.deleteByBoardNumber(boardNumber);
        boardRepository.delete(board);
    }


}
