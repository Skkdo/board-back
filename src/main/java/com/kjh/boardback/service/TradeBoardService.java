package com.kjh.boardback.service;

import com.kjh.boardback.dto.request.trade_board.PatchTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PatchTradeCommentRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeCommentRequestDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeBoardListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeBoardResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeCommentListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.entity.SearchLog;
import com.kjh.boardback.entity.User;
import com.kjh.boardback.entity.trade_board.TradeBoard;
import com.kjh.boardback.entity.trade_board.TradeComment;
import com.kjh.boardback.entity.trade_board.TradeFavorite;
import com.kjh.boardback.entity.trade_board.TradeImage;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.exception.BusinessException;
import com.kjh.boardback.repository.SearchLogRepository;
import com.kjh.boardback.repository.trade_board.TradeBoardRepository;
import com.kjh.boardback.repository.trade_board.TradeCommentRepository;
import com.kjh.boardback.repository.trade_board.TradeFavoriteRepository;
import com.kjh.boardback.repository.trade_board.TradeImageRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private final TradeImageRepository imageRepository;
    private final TradeCommentRepository commentRepository;
    private final TradeFavoriteRepository favoriteRepository;
    private final UserService userService;
    private final SearchLogRepository searchLogRepository;

    public TradeBoard findByBoardNumber(Integer boardNumber) {
        return boardRepository.findByBoardNumber(boardNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_BOARD));
    }

    public TradeComment findByCommentNumber(Integer commentNumber) {
        return commentRepository.findByCommentNumber(commentNumber).orElseThrow(
                () -> new BusinessException(ResponseCode.NOT_EXISTED_COMMENT));
    }

    public GetTradeBoardResponseDto getBoard(Integer boardNumber) {
        TradeBoard board = findByBoardNumber(boardNumber);
        List<TradeImage> imageList = imageRepository.findByBoard_BoardNumber(boardNumber);
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
        searchLogRepository.save(searchLog);

        boolean relation = preSearchWord != null;
        if (relation) {
            searchLog = new SearchLog(preSearchWord, searchWord, relation);
            searchLogRepository.save(searchLog);
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
        imageRepository.saveAll(imageEntities);
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

        imageRepository.deleteByBoard_BoardNumber(boardNumber);
        List<TradeImage> imageList = new ArrayList<>();
        List<String> boardImageList = dto.getBoardImageList();
        for (String image : boardImageList) {
            TradeImage imageEntity = TradeImage.from(board, image);
            imageList.add(imageEntity);
        }
        imageRepository.saveAll(imageList);
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

        imageRepository.deleteByBoard_BoardNumber(boardNumber);
        favoriteRepository.deleteByBoard_BoardNumber(boardNumber);
        commentRepository.deleteByBoard_BoardNumber(boardNumber);
        boardRepository.delete(board);
    }

    public GetTradeCommentListResponseDto getCommentList(Integer boardNumber) {
        findByBoardNumber(boardNumber);
        List<TradeComment> commentList = commentRepository.getCommentList(boardNumber);
        return new GetTradeCommentListResponseDto(commentList);
    }

    @Transactional
    public void postComment(Integer boardNumber, String email, PostTradeCommentRequestDto dto) {

        TradeBoard board = findByBoardNumber(boardNumber);
        User user = userService.findByEmailOrElseThrow(email);

        TradeComment comment = TradeComment.from(user, board, dto);
        commentRepository.save(comment);

        board.increaseCommentCount();
        boardRepository.save(board);
    }

    @Transactional
    public void patchComment(Integer boardNumber, Integer commentNumber, String email,
                             PatchTradeCommentRequestDto dto) {

        userService.findByEmailOrElseThrow(email);
        findByBoardNumber(boardNumber);
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
        TradeBoard board = findByBoardNumber(boardNumber);
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
        boardRepository.save(board);

    }

    public GetTradeFavoriteListResponseDto getFavoriteList(Integer boardNumber) {
        findByBoardNumber(boardNumber);
        List<TradeFavorite> favoriteList = favoriteRepository.getFavoriteList(boardNumber);
        return new GetTradeFavoriteListResponseDto(favoriteList);
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
