package com.kjh.boardback.service;

import com.kjh.boardback.dto.request.trade_board.PatchTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PatchTradeCommentRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeBoardRequestDto;
import com.kjh.boardback.dto.request.trade_board.PostTradeCommentRequestDto;
import com.kjh.boardback.dto.response.ResponseDto;
import com.kjh.boardback.dto.response.trade_board.DeleteTradeBoardResponseDto;
import com.kjh.boardback.dto.response.trade_board.DeleteTradeCommentResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetLatestTradeBoardListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetSearchTradeBoardListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTop3TradeBoardListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeBoardResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeCommentListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetTradeFavoriteListResponseDto;
import com.kjh.boardback.dto.response.trade_board.GetUserTradeBoardListResponseDto;
import com.kjh.boardback.dto.response.trade_board.IncreaseTradeViewCountResponseDto;
import com.kjh.boardback.dto.response.trade_board.PatchTradeBoardResponseDto;
import com.kjh.boardback.dto.response.trade_board.PatchTradeCommentResponseDto;
import com.kjh.boardback.dto.response.trade_board.PostTradeBoardResponseDto;
import com.kjh.boardback.dto.response.trade_board.PostTradeCommentResponseDto;
import com.kjh.boardback.dto.response.trade_board.PutTradeFavoriteResponseDto;
import com.kjh.boardback.entity.SearchLogEntity;
import com.kjh.boardback.entity.trade_board.TradeBoardEntity;
import com.kjh.boardback.entity.trade_board.TradeBoardListViewEntity;
import com.kjh.boardback.entity.trade_board.TradeCommentEntity;
import com.kjh.boardback.entity.trade_board.TradeFavoriteEntity;
import com.kjh.boardback.entity.trade_board.TradeImageEntity;
import com.kjh.boardback.repository.SearchLogRepository;
import com.kjh.boardback.repository.UserRepository;
import com.kjh.boardback.repository.resultSet.GetTradeBoardResultSet;
import com.kjh.boardback.repository.resultSet.GetTradeCommentListResultSet;
import com.kjh.boardback.repository.resultSet.GetTradeFavoriteListResultSet;
import com.kjh.boardback.repository.trade_board.TradeBoardListViewRepository;
import com.kjh.boardback.repository.trade_board.TradeBoardRepository;
import com.kjh.boardback.repository.trade_board.TradeCommentRepository;
import com.kjh.boardback.repository.trade_board.TradeFavoriteRepository;
import com.kjh.boardback.repository.trade_board.TradeImageRepository;
import com.kjh.boardback.service.TradeBoardService;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeBoardServiceImplement implements TradeBoardService {

    private final TradeBoardRepository tradeBoardRepository;
    private final TradeImageRepository tradeImageRepository;
    private final TradeCommentRepository tradeCommentRepository;
    private final TradeFavoriteRepository tradeFavoriteRepository;
    private final TradeBoardListViewRepository tradeBoardListViewRepository;
    private final UserRepository userRepository;
    private final SearchLogRepository searchLogRepository;

    @Override
    public ResponseEntity<? super PostTradeCommentResponseDto> postComment(Integer boardNumber, String email, PostTradeCommentRequestDto dto) {
        try {
            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PostTradeCommentResponseDto.noExistBoard();

            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PostTradeCommentResponseDto.noExistUser();

            TradeCommentEntity commentEntity = new TradeCommentEntity(boardNumber, email, dto);
            tradeCommentRepository.save(commentEntity);

            boardEntity.increaseCommentCount();
            tradeBoardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            ResponseDto.databaseError();
        }
        return PostTradeCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetLatestTradeBoardListResponseDto> getLatestBoardList() {

        List<TradeBoardListViewEntity> tradeBoardListViewEntities = new ArrayList<>();

        try {
            tradeBoardListViewEntities = tradeBoardListViewRepository.findByOrderByWriteDatetimeDesc();

        } catch (Exception exception) {
            exception.printStackTrace();
            return GetLatestTradeBoardListResponseDto.databaseError();
        }
        return GetLatestTradeBoardListResponseDto.success(tradeBoardListViewEntities);
    }

    @Override
    public ResponseEntity<? super DeleteTradeBoardResponseDto> deleteBoard(String email, Integer boardNumber) {
        try {
            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return DeleteTradeBoardResponseDto.noExistUser();

            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return DeleteTradeBoardResponseDto.noExistBoard();

            boolean isWriter = boardEntity.getWriterEmail().equals(email);
            if (!isWriter) return DeleteTradeBoardResponseDto.noPermission();

            tradeImageRepository.deleteByBoardNumber(boardNumber);
            tradeCommentRepository.deleteByBoardNumber(boardNumber);
            tradeFavoriteRepository.deleteByBoardNumber(boardNumber);
            tradeBoardRepository.delete(boardEntity);


        } catch (Exception exception) {
            exception.printStackTrace();
            return DeleteTradeBoardResponseDto.databaseError();
        }
        return DeleteTradeBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PatchTradeBoardResponseDto> patchBoard(PatchTradeBoardRequestDto requestDto, String email, Integer boardNumber) {
        try {
            boolean existedUser = userRepository.existsByEmail(email);
            if (!existedUser) return PatchTradeBoardResponseDto.noExistUser();

            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PatchTradeBoardResponseDto.noExistBoard();

            boolean isWriter = boardEntity.getWriterEmail().equals(email);
            if (!isWriter) return PatchTradeBoardResponseDto.noPermission();

            boardEntity.patchBoard(requestDto);
            tradeBoardRepository.save(boardEntity);

            tradeImageRepository.deleteByBoardNumber(boardNumber);
            List<String> imageList = requestDto.getBoardImageList();
            List<TradeImageEntity> imageEntities = new ArrayList<>();

            for (String image : imageList) {
                TradeImageEntity imageEntity = new TradeImageEntity(boardNumber, image);
                imageEntities.add(imageEntity);
            }
            tradeImageRepository.saveAll(imageEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return PatchTradeBoardResponseDto.databaseError();
        }
        return PatchTradeBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostTradeBoardResponseDto> postBoard(PostTradeBoardRequestDto requestDto, String email) {
        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return PostTradeBoardResponseDto.noExistUser();

            TradeBoardEntity tradeBoardEntity = new TradeBoardEntity(requestDto, email);
            tradeBoardRepository.save(tradeBoardEntity);

            int boardNumber = tradeBoardEntity.getBoardNumber();

            List<String> imageList = requestDto.getBoardImageList();
            List<TradeImageEntity> imageEntities = new ArrayList<>();

            for (String image : imageList) {
                TradeImageEntity imageEntity = new TradeImageEntity(boardNumber, image);
                imageEntities.add(imageEntity);
            }
            tradeImageRepository.saveAll(imageEntities);

        } catch (Exception exception) {
            exception.printStackTrace();
            return PostTradeBoardResponseDto.databaseError();
        }
        return PostTradeBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetTradeBoardResponseDto> getBoard(Integer boardNumber) {

        GetTradeBoardResultSet resultSet = null;
        List<TradeImageEntity> tradeImageEntities = new ArrayList<>();

        try {
            resultSet = tradeBoardRepository.getTradeBoard(boardNumber);
            if (resultSet == null) return GetTradeBoardResponseDto.noExistBoard();

            tradeImageEntities = tradeImageRepository.findByBoardNumber(boardNumber);


        } catch (Exception exception) {
            exception.printStackTrace();
            return GetTradeBoardResponseDto.databaseError();
        }
        return GetTradeBoardResponseDto.success(resultSet, tradeImageEntities);
    }

    @Override
    public ResponseEntity<? super GetTradeFavoriteListResponseDto> getFavoriteList(Integer boardNumber) {
        List<GetTradeFavoriteListResultSet> resultSets = new ArrayList<>();

        try {
            Boolean existedBoard = tradeBoardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetTradeFavoriteListResponseDto.noExistBoard();

            resultSets = tradeFavoriteRepository.getFavoriteList(boardNumber);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetTradeFavoriteListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super GetTradeCommentListResponseDto> getCommentList(Integer boardNumber) {
        List<GetTradeCommentListResultSet> resultSets = new ArrayList<>();

        try {
            Boolean existedBoard = tradeBoardRepository.existsByBoardNumber(boardNumber);
            if (!existedBoard) return GetTradeCommentListResponseDto.noExistBoard();

            resultSets = tradeCommentRepository.getCommentList(boardNumber);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetTradeCommentListResponseDto.success(resultSets);
    }

    @Override
    public ResponseEntity<? super IncreaseTradeViewCountResponseDto> increaseViewCount(Integer boardNumber) {
        try {
            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return IncreaseTradeViewCountResponseDto.noExistBoard();
            boardEntity.increaseViewCount();
            tradeBoardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return IncreaseTradeViewCountResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetTop3TradeBoardListResponseDto> getTop3BoardList() {
        List<TradeBoardListViewEntity> boardListViewEntities = new ArrayList<>();

        try {
            Date beforeWeek = Date.from(Instant.now().minus(7, ChronoUnit.DAYS));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sevenDaysAgo = simpleDateFormat.format(beforeWeek);
            boardListViewEntities = tradeBoardListViewRepository.getTop3BoardList(sevenDaysAgo);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetTop3TradeBoardListResponseDto.success(boardListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetSearchTradeBoardListResponseDto> getSearchBoardList(String searchWord, String preSearchWord) {

        List<TradeBoardListViewEntity> boardListViewEntities = new ArrayList<>();

        try {
            boardListViewEntities = tradeBoardListViewRepository.getSearchBoardList(searchWord, searchWord,searchWord);
            SearchLogEntity searchLogEntity = new SearchLogEntity(searchWord, preSearchWord, false);
            searchLogRepository.save(searchLogEntity);

            boolean relation = preSearchWord != null;
            if (relation) {
                searchLogEntity = new SearchLogEntity(preSearchWord, searchWord, relation);
                searchLogRepository.save(searchLogEntity);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetSearchTradeBoardListResponseDto.success(boardListViewEntities);
    }

    @Override
    public ResponseEntity<? super GetUserTradeBoardListResponseDto> getUserBoardList(String email) {
        List<TradeBoardListViewEntity> boardListViewEntities = new ArrayList<>();

        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return GetUserTradeBoardListResponseDto.noExistUser();

            boardListViewEntities = tradeBoardListViewRepository.findByWriterEmailOrderByWriteDatetimeDesc(email);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetUserTradeBoardListResponseDto.success(boardListViewEntities);
    }

    @Override
    public ResponseEntity<? super PatchTradeCommentResponseDto> patchComment(Integer boardNumber, Integer commentNumber, String email, PatchTradeCommentRequestDto dto) {
        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return PatchTradeCommentResponseDto.noExistUser();

            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PatchTradeCommentResponseDto.noExistBoard();

            TradeCommentEntity commentEntity = tradeCommentRepository.findByCommentNumber(commentNumber);
            if (commentEntity == null) return PatchTradeCommentResponseDto.noExistBoard();

            String commentWriterEmail = commentEntity.getUserEmail();
            boolean isCommentWriter = commentWriterEmail.equals(email);

            if (!isCommentWriter) return PatchTradeCommentResponseDto.noPermission();

            commentEntity.patchComment(dto);
            tradeCommentRepository.save(commentEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PatchTradeCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteTradeCommentResponseDto> deleteComment(Integer boardNumber, Integer commentNumber, String email) {
        try {
            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return DeleteTradeCommentResponseDto.noExistBoard();

            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return DeleteTradeCommentResponseDto.noExistUser();

            TradeCommentEntity commentEntity = tradeCommentRepository.findByCommentNumber(commentNumber);
            if (commentEntity == null) return DeleteTradeCommentResponseDto.noExistBoard();

            String writerEmail = boardEntity.getWriterEmail();
            String commentWriterEmail = commentEntity.getUserEmail();
            boolean isWriter = writerEmail.equals(email);
            boolean isCommentWriter = commentWriterEmail.equals(email);

            if (!isWriter && !isCommentWriter) return DeleteTradeCommentResponseDto.noPermission();


            tradeCommentRepository.delete(commentEntity);
            boardEntity.decreaseCommentCount();
            tradeBoardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteTradeCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PutTradeFavoriteResponseDto> putFavorite(String email, Integer boardNumber) {
        try {
            boolean existedEmail = userRepository.existsByEmail(email);
            if (!existedEmail) return PutTradeFavoriteResponseDto.noExistUser();

            TradeBoardEntity boardEntity = tradeBoardRepository.findByBoardNumber(boardNumber);
            if (boardEntity == null) return PutTradeFavoriteResponseDto.noExistBoard();

            TradeFavoriteEntity favoriteEntity = tradeFavoriteRepository.findByBoardNumberAndUserEmail(boardNumber, email);
            if (favoriteEntity == null) {
                favoriteEntity = new TradeFavoriteEntity(email, boardNumber);
                tradeFavoriteRepository.save(favoriteEntity);
                boardEntity.increaseFavoriteCount();
            } else {
                tradeFavoriteRepository.delete(favoriteEntity);
                boardEntity.decreaseFavoriteCount();
            }
            tradeBoardRepository.save(boardEntity);

        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return PutTradeFavoriteResponseDto.success();
    }
}
